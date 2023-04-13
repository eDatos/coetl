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

import es.gobcan.istac.coetl.config.ApacheHopProperties;
import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.platform.hop.service.impl.HopExecutionServiceImpl;
import es.gobcan.istac.coetl.platform.hop.enumeration.WorkflowMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.enumeration.PipelineMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.service.util.HopUtil;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.EtlStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.WorkflowStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.PipelineStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.WebResultDTO;
import es.gobcan.istac.coetl.service.ExecutionService;

@Component
public class ApacheHopWatchJob {

    private static final Logger LOG = LoggerFactory.getLogger(ApacheHopWatchJob.class);

    private final ExecutionService executionService;

    private final HopExecutionServiceImpl hopExecutionService;
    
    private final GitService gitService;

    private final String url;
    private final String user;
    private final String password;

    public ApacheHopWatchJob(ApacheHopProperties apacheHopProperties, ExecutionService executionService, HopExecutionServiceImpl hopExecutionService, GitService gitService) {
        this.executionService = executionService;
        this.hopExecutionService = hopExecutionService;
        this.url = HopUtil.getUrl(apacheHopProperties);
        this.user = HopUtil.getUser(apacheHopProperties);
        this.password = HopUtil.getPassword(apacheHopProperties);
        this.gitService = gitService;
    }

    @Scheduled(cron = Constants.DEFAULT_PLATFORM_WATCH_CRON)
    @Transactional
    public void run() {
        LOG.info("Init Apache Hop watch job");
        List<Execution> runningExecutions = executionService.getInRunningResultAndEtlExecutionPlatform(TipoPlataformaEjecucion.APACHE_HOP);

        if (runningExecutions != null && !runningExecutions.isEmpty()) {
            for (Execution runningExecution : runningExecutions) {
                Etl runningEtl = runningExecution.getEtl();
                LOG.info("Watching running HOP ETL {}", runningEtl.getCode());
                final String etlFilename = gitService.getMainFileName(runningEtl);
                EtlStatusDTO etlStatusDTO;
                if (runningEtl.isPipeline()) {
                    etlStatusDTO = executeStatusPipeline(etlFilename, runningExecution.getIdExecution());
                } else {
                    etlStatusDTO = executeStatusWorkflow(etlFilename, runningExecution.getIdExecution());
                }

                if (etlStatusDTO.isFinished()) {
                    LOG.info("HOP ETL {} finished", runningEtl.getCode());
                    Execution finishedExecution = updateExecutionFromEtlStatus(runningExecution, etlStatusDTO);
                    executionService.update(finishedExecution);
                    hopExecutionService.removeEtl(runningEtl, etlFilename, runningExecution.getIdExecution());
                } else {
                    LOG.info("HOP ETL {} not finished yet", runningEtl.getCode());
                }
            }
        }

        Execution nextExecution = executionService.getOldestInWaitingResultAndEtlExecutionPlatform(TipoPlataformaEjecucion.APACHE_HOP);
        if (nextExecution == null) {
            LOG.info("There is not HOP ETL to execute.");
            return;
        }

        Etl nextEtl = nextExecution.getEtl();
        final String etlFilename = gitService.getMainFileName(nextEtl);
        WebResultDTO webResultDTO = hopExecutionService.runEtl(nextEtl, etlFilename, nextExecution.getIdExecution());

        Execution nextExecutionResult;
        if (!webResultDTO.isOk()) {
            LOG.error("Error executing next HOP ETL {} - cause: {}", nextEtl.getCode(), webResultDTO.getMessage());
            hopExecutionService.notifyExecutionError(nextEtl);
            hopExecutionService.removeEtl(nextEtl, etlFilename, nextExecution.getIdExecution());
            nextExecution.setStartDate(Instant.now());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.FAILED, webResultDTO.getMessage());
        } else {
            LOG.info("Executing next HOP etl {}", nextEtl.getCode());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.RUNNING);
        }
        executionService.update(nextExecutionResult);
    }

    private EtlStatusDTO executeStatusPipeline(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, PipelineMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, PipelineStatusDTO.class).getBody();
    }

    private EtlStatusDTO executeStatusWorkflow(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, WorkflowMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, WorkflowStatusDTO.class).getBody();
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
