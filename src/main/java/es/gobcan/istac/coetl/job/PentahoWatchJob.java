package es.gobcan.istac.coetl.job;

import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.config.PentahoProperties;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.platform.pentaho.enumeration.JobMethodsEnum;
import es.gobcan.istac.coetl.platform.pentaho.enumeration.Status;
import es.gobcan.istac.coetl.platform.pentaho.enumeration.TransMethodsEnum;
import es.gobcan.istac.coetl.platform.pentaho.service.impl.PentahoExecutionServiceImpl;
import es.gobcan.istac.coetl.platform.pentaho.service.util.PentahoUtil;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.EtlStatusDTO;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.JobStatusDTO;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.TransStatusDTO;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.WebResultDTO;
import es.gobcan.istac.coetl.service.ExecutionService;

@Component
public class PentahoWatchJob {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoWatchJob.class);

    private final ExecutionService executionService;

    private final PentahoExecutionServiceImpl pentahoExecutionService;
    
    private final GitService gitService;

    private final String url;
    private final String user;
    private final String password;

    public PentahoWatchJob(PentahoProperties pentahoProperties, ExecutionService executionService, PentahoExecutionServiceImpl pentahoExecutionService, GitService gitService) {
        this.executionService = executionService;
        this.pentahoExecutionService = pentahoExecutionService;
        this.url = PentahoUtil.getUrl(pentahoProperties);
        this.user = PentahoUtil.getUser(pentahoProperties);
        this.password = PentahoUtil.getPassword(pentahoProperties);
        this.gitService = gitService;
    }

    @Scheduled(cron = Constants.DEFAULT_PLATFORM_WATCH_CRON)
    @Transactional
    public void run() {
        LOG.info("Init Pentaho watch job");
        List<Execution> runningExecutions = executionService.getInRunningResultAndEtlExecutionPlatform(TipoPlataformaEjecucion.PENTAHO);

        if (runningExecutions != null && !runningExecutions.isEmpty()) {
            for (Execution runningExecution : runningExecutions) {
                Etl runningEtl = runningExecution.getEtl();
                LOG.info("Watching running PENTAHO ETL {}", runningEtl.getCode());
                final String etlFilename = gitService.getMainFileName(runningEtl);
                EtlStatusDTO etlStatusDTO;
                if (runningEtl.isTransformation()) {
                    etlStatusDTO = runExecuteStatusTrans(etlFilename, runningExecution, runningEtl);
                } else {
                    etlStatusDTO = runExecuteStatusJob(etlFilename, runningExecution, runningEtl);
                }

                if (etlStatusDTO.isFinished()) {
                    LOG.info("PENTAHO ETL {} finished", runningEtl.getCode());
                    Execution finishedExecution = updateExecutionFromEtlStatus(runningExecution, etlStatusDTO);
                    executionService.update(finishedExecution);
                    runExecuteRemoveEtl(runningEtl, etlFilename, runningExecution);
                } else {
                    LOG.info("PENTAHO ETL {} not finished yet", runningEtl.getCode());
                }
            }
        }

        Execution nextExecution = executionService.getOldestInWaitingResultAndEtlExecutionPlatform(TipoPlataformaEjecucion.PENTAHO);
        if (nextExecution == null) {
            LOG.info("There is not PENTAHO ETL to execute.");
            return;
        }

        Etl nextEtl = nextExecution.getEtl();
        final String etlFilename = gitService.getMainFileName(nextEtl);
        WebResultDTO webResultDTO = pentahoExecutionService.runEtl(nextEtl, etlFilename, nextExecution.getIdExecution());

        Execution nextExecutionResult;
        if (!webResultDTO.isOk()) {
            LOG.error("Error executing next PENTAHO ETL {} - cause: {}", nextEtl.getCode(), webResultDTO.getMessage());
            pentahoExecutionService.notifyExecutionError(nextEtl);
            runExecuteRemoveEtl(nextEtl, etlFilename, nextExecution);
            nextExecution.setStartDate(Instant.now());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.FAILED, webResultDTO.getMessage());
        } else {
            LOG.info("Executing next PENTAHO etl {}", nextEtl.getCode());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.RUNNING);
        }
        executionService.update(nextExecutionResult);
    }
    
    private void runExecuteRemoveEtl(Etl runningEtl, String etlFilename, Execution runningExecution) {
        try {
            pentahoExecutionService.removeEtl(runningEtl, etlFilename, runningExecution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred removing pentaho execution ({}): {}", runningEtl.getName(), e.getMessage());
        }
    }

    private EtlStatusDTO runExecuteStatusTrans(String etlFilename, Execution runningExecution, Etl runningEtl) {
        EtlStatusDTO etlStatusDTO;
        try {
            etlStatusDTO = executeStatusTrans(etlFilename, runningExecution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred during transform execution ({}): {}", runningEtl.getName(), e.getMessage());
            etlStatusDTO = new TransStatusDTO();
            etlStatusDTO.setErrorDescription(e.getMessage());
            etlStatusDTO.setStatus(Status.FINISHED_WITH_ERRORS);
        }
        return etlStatusDTO;
    }

    private EtlStatusDTO executeStatusTrans(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, TransStatusDTO.class).getBody();
    }

    private EtlStatusDTO runExecuteStatusJob(String etlFilename, Execution runningExecution, Etl runningEtl) {
        EtlStatusDTO etlStatusDTO;
        try {
            etlStatusDTO = executeStatusJob(etlFilename, runningExecution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred during job execution ({}): {}", runningEtl.getName(), e.getMessage());
            etlStatusDTO = new JobStatusDTO();
            etlStatusDTO.setErrorDescription(e.getMessage());
            etlStatusDTO.setStatus(Status.FINISHED_WITH_ERRORS);
        }
        return etlStatusDTO;
    }

    private EtlStatusDTO executeStatusJob(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, JobStatusDTO.class).getBody();
    }

    private Execution updateExecutionFromEtlStatus(Execution currentExecution, EtlStatusDTO etlStatusDTO) {
        if (etlStatusDTO.isFinishedWithErrors() || etlStatusDTO.isStoppedWithErrors() || etlStatusDTO.isStopped()) {
            return updateExecutionFromResult(currentExecution, Result.FAILED, etlStatusDTO.getErrorDescription());
        }
        return updateExecutionFromResult(currentExecution, Result.SUCCESS);
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Result result, String notes) {
        if (Result.RUNNING.equals(result)) {
            currentExecution.setStartDate(Instant.now());
        }
        if (Result.FAILED.equals(result) || Result.SUCCESS.equals(result)) {
            currentExecution.setFinishDate(Instant.now());
        }
        currentExecution.setResult(result);
        currentExecution.setNotes(notes);
        return currentExecution;
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Result result) {
        return updateExecutionFromResult(currentExecution, result, null);
    }
}
