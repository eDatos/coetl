package es.gobcan.coetl.job;

import java.time.ZonedDateTime;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import es.gobcan.coetl.config.PentahoProperties;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.pentaho.enumeration.JobMethodsEnum;
import es.gobcan.coetl.pentaho.enumeration.TransMethodsEnum;
import es.gobcan.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.coetl.pentaho.service.util.PentahoUtil;
import es.gobcan.coetl.pentaho.web.rest.dto.EtlStatusDTO;
import es.gobcan.coetl.pentaho.web.rest.dto.JobStatusDTO;
import es.gobcan.coetl.pentaho.web.rest.dto.TransStatusDTO;
import es.gobcan.coetl.pentaho.web.rest.dto.WebResultDTO;
import es.gobcan.coetl.service.ExecutionService;

@Component
public class PentahoWatchJob {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoWatchJob.class);

    private final ExecutionService executionService;

    private final PentahoExecutionService pentahoExecutionService;

    private final String url;
    private final String user;
    private final String password;

    public PentahoWatchJob(PentahoProperties pentahoProperties, ExecutionService executionService, PentahoExecutionService pentahoExecutionService) {
        this.executionService = executionService;
        this.pentahoExecutionService = pentahoExecutionService;
        this.url = PentahoUtil.getUrl(pentahoProperties);
        this.user = PentahoUtil.getUser(pentahoProperties);
        this.password = PentahoUtil.getPassword(pentahoProperties);
    }

    @Scheduled(cron = "${application.watcher.cron:0 * * * * *}")
    @Transactional
    public void run() {
        LOG.info("Init Pentaho watch job");
        Execution runningExecution = executionService.getInRunningResult();

        if (runningExecution != null) {
            Etl runningEtl = runningExecution.getEtl();
            LOG.info("Watching running ETL {}", runningEtl.getCode());
            final String etlFilename = PentahoUtil.getFileBasename(runningEtl.getEtlFile().getName());
            EtlStatusDTO etlStatusDTO;
            if (runningEtl.isEtl()) {
                etlStatusDTO = executeStatusTrans(etlFilename);
            } else {
                etlStatusDTO = executeStatusJob(etlFilename);
            }

            if (etlStatusDTO.isFinished()) {
                LOG.info("ETL {} finished", runningEtl.getCode());
                Execution finishedExecution = updateExecutionFromEtlStatus(runningExecution, etlStatusDTO);
                executionService.update(finishedExecution);
                pentahoExecutionService.removeEtl(runningEtl, etlFilename);
            } else {
                LOG.info("ETL {} not finished yet", runningEtl.getCode());
                return;
            }
        }

        Execution nextExecution = executionService.getOldestInWaitingResult();
        if (nextExecution == null) {
            LOG.info("There is not ETL to execute.");
            return;
        }

        Etl nextEtl = nextExecution.getEtl();
        final String etlFilename = PentahoUtil.getFileBasename(nextEtl.getEtlFile().getName());
        WebResultDTO webResultDTO = pentahoExecutionService.runEtl(nextEtl, etlFilename);

        Execution nextExecutionResult;
        if (!webResultDTO.isOk()) {
            LOG.error("Error executing next ETL {} - cause: {}", nextEtl.getCode(), webResultDTO.getMessage());
            pentahoExecutionService.removeEtl(nextEtl, etlFilename);
            nextExecution.setStartDate(ZonedDateTime.now());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.FAILED, webResultDTO.getMessage());
        } else {
            LOG.info("Executing next etl {}", nextEtl.getCode());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.RUNNING);
        }
        executionService.update(nextExecutionResult);
    }

    private EtlStatusDTO executeStatusTrans(String etlFilename) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, TransStatusDTO.class).getBody();
    }

    private EtlStatusDTO executeStatusJob(String etlFilename) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, JobStatusDTO.class).getBody();
    }

    private Execution updateExecutionFromEtlStatus(Execution currentExecution, EtlStatusDTO etlStatusDTO) {
        if (etlStatusDTO.isFinishedWithErrors()) {
            return updateExecutionFromResult(currentExecution, Result.FAILED, etlStatusDTO.getErrorDescription());
        }
        return updateExecutionFromResult(currentExecution, Result.SUCCESS);
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Result result, String notes) {
        if (Result.RUNNING.equals(result)) {
            currentExecution.setStartDate(ZonedDateTime.now());
        }
        if (Result.FAILED.equals(result) || Result.SUCCESS.equals(result)) {
            currentExecution.setFinishDate(ZonedDateTime.now());
        }
        currentExecution.setResult(result);
        currentExecution.setNotes(notes);
        return currentExecution;
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Result result) {
        return updateExecutionFromResult(currentExecution, result, null);
    }
}
