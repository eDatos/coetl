package es.gobcan.coetl.pentaho.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import es.gobcan.coetl.config.PentahoProperties;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.pentaho.enumeration.JobMethodsEnum;
import es.gobcan.coetl.pentaho.enumeration.TransMethodsEnum;
import es.gobcan.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.coetl.pentaho.service.PentahoWatchService;
import es.gobcan.coetl.pentaho.service.util.PentahoUtil;
import es.gobcan.coetl.pentaho.web.rest.dto.EtlStatusDTO;
import es.gobcan.coetl.pentaho.web.rest.dto.JobStatusDTO;
import es.gobcan.coetl.pentaho.web.rest.dto.TransStatusDTO;
import es.gobcan.coetl.pentaho.web.rest.dto.WebResultDTO;
import es.gobcan.coetl.service.ExecutionService;

@Service
public class PentahoWatchServiceImpl implements PentahoWatchService {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoWatchServiceImpl.class);

    private final ExecutionService executionService;

    private final PentahoExecutionService pentahoExecutionService;

    private final MessageSource messageSource;

    private final String url;
    private final String user;
    private final String password;

    public PentahoWatchServiceImpl(PentahoProperties pentahoProperties, ExecutionService executionService, PentahoExecutionService pentahoExecutionService, MessageSource messageSource) {
        this.executionService = executionService;
        this.pentahoExecutionService = pentahoExecutionService;
        this.messageSource = messageSource;
        this.url = PentahoUtil.getUrl(pentahoProperties);
        this.user = PentahoUtil.getUser(pentahoProperties);
        this.password = PentahoUtil.getPassword(pentahoProperties);
    }

    @Override
    @Scheduled(cron = "${application.watcher.cron:0 * * * * *}")
    @Transactional
    public void run() {
        LOG.debug("Init watcher!");
        Execution runningExecution = executionService.getInRunningResult();

        if (runningExecution != null) {
            Etl runningEtl = runningExecution.getEtl();
            final String etlFilename = PentahoUtil.getFileBasename(runningEtl.getEtlFile().getName());
            EtlStatusDTO etlStatusDTO;
            if (runningEtl.isEtl()) {
                etlStatusDTO = executeStatusTrans(etlFilename);
            } else {
                etlStatusDTO = executeStatusJob(etlFilename);
            }

            if (etlStatusDTO.isFinished()) {
                Execution finishedExecution = updateExecutionFromEtlStatus(runningExecution, etlStatusDTO);
                executionService.update(finishedExecution);
                pentahoExecutionService.removeEtl(runningEtl, etlFilename);
            } else {
                return;
            }
        }

        Execution nextExecution = executionService.getOldestInWaitingResult();
        if (nextExecution == null) {
            return;
        }

        Etl nextEtl = nextExecution.getEtl();
        final String etlFilename = PentahoUtil.getFileBasename(nextEtl.getEtlFile().getName());
        WebResultDTO webResultDTO = pentahoExecutionService.runEtl(nextEtl, etlFilename);

        Execution nextExecutionResult;
        if (!webResultDTO.isOk()) {
            pentahoExecutionService.removeEtl(nextEtl, etlFilename);
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.FAILED, webResultDTO.getMessage());
        } else {
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
        currentExecution.setResult(result);
        currentExecution.setNotes(notes);
        return currentExecution;
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Result result) {
        return updateExecutionFromResult(currentExecution, result, null);
    }
}
