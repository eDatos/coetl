package es.gobcan.istac.coetl.job;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecutionEtl;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.platform.hop.enumeration.PipelineMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.enumeration.Status;
import es.gobcan.istac.coetl.platform.hop.enumeration.WorkflowMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.service.impl.HopExecutionServiceImpl;
import es.gobcan.istac.coetl.platform.hop.service.util.HopUtil;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.EtlStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.PipelineStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.WebResultDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.WorkflowStatusDTO;
import es.gobcan.istac.coetl.service.ComputationalThreadExecutionService;
import es.gobcan.istac.coetl.service.ComputationalThreadsService;
import es.gobcan.istac.coetl.service.ExecutionService;

@Component
public class ApacheHopWatchJob {

    private static final Logger LOG = LoggerFactory.getLogger(ApacheHopWatchJob.class);
    private static final String MSG_ERROR_EXECUTION_OTHERS_ETL_THREAD = "No se pudo ejecutar la ETL debido a que otra ETL previa del hilo computacional (%s) falló";

    private final ExecutionService executionService;

    private final HopExecutionServiceImpl hopExecutionService;
    
    private final GitService gitService;
    
    private final ComputationalThreadExecutionService computationalThreadExecutionService;

    private final ComputationalThreadsService computationalThreadsService;

    private final String url;
    private final String user;
    private final String password;

    public ApacheHopWatchJob(ApacheHopProperties apacheHopProperties, ExecutionService executionService, HopExecutionServiceImpl hopExecutionService, GitService gitService,
            ComputationalThreadExecutionService computationalThreadExecutionService, ComputationalThreadsService computationalThreadsService) {
        this.executionService = executionService;
        this.hopExecutionService = hopExecutionService;
        this.url = HopUtil.getUrl(apacheHopProperties);
        this.user = HopUtil.getUser(apacheHopProperties);
        this.password = HopUtil.getPassword(apacheHopProperties);
        this.gitService = gitService;
        this.computationalThreadExecutionService = computationalThreadExecutionService;
        this.computationalThreadsService = computationalThreadsService;
    }

    @Scheduled(cron = Constants.DEFAULT_PLATFORM_WATCH_CRON)
    @Transactional
    public void run() {
        LOG.info("Init Apache Hop watch job");
        List<Execution> runningExecutions = executionService.getInRunningResultAndEtlExecutionPlatform(TipoPlataformaEjecucion.APACHE_HOP);
        List<ComputationalThreadExecution> waitingThreadExecutions = computationalThreadExecutionService.getThreadsExecutionByResult(ComputationalThreadExecution.Result.WAITING);
        List<ComputationalThreadExecutionEtl> currentRunningNotFinished = new ArrayList<>();

        if (runningExecutions != null && !runningExecutions.isEmpty()) {
            for (Execution runningExecution : runningExecutions) {
                Etl runningEtl = runningExecution.getEtl();
                LOG.info("Watching running HOP ETL {}", runningEtl.getCode());
                final String etlFilename = gitService.getMainFileName(runningEtl);
                EtlStatusDTO etlStatusDTO;
                if (runningEtl.isPipeline()) {
                    etlStatusDTO = runExecuteStatusPipeline(etlFilename, runningExecution, runningEtl);
                } else {
                    etlStatusDTO = runExecuteStatusWorkflow(etlFilename, runningExecution, runningEtl);
                }

                ComputationalThreadExecutionEtl currentThreadEtlExecution = computationalThreadExecutionService.findByExecutionId(runningExecution.getId());
                if (!etlStatusDTO.isFinished() && currentThreadEtlExecution != null) {
                    currentRunningNotFinished.add(currentThreadEtlExecution);
                }

                if (etlStatusDTO.isFinished()) {
                    LOG.info("HOP ETL {} finished", runningEtl.getCode());
                    Execution finishedExecution = updateExecutionFromEtlStatus(runningExecution, etlStatusDTO);
                    executionService.update(finishedExecution);
                    runExecuteRemoveEtl(runningEtl, etlFilename, runningExecution);

                    if (currentThreadEtlExecution != null) {
                        ComputationalThreadExecution currentThreadExecution = computationalThreadExecutionService
                                .findOne(currentThreadEtlExecution.getComputationalThreadExecution().getId());
                        List<ComputationalThreadExecutionEtl> etlsThreadExecution = computationalThreadExecutionService
                                .findAllByComputationalThreadExecutionId(currentThreadEtlExecution.getComputationalThreadExecution().getId());
                        if (isExecutionFailed(etlStatusDTO)) {
                            finishExecutionThreadForError(etlsThreadExecution, finishedExecution, currentThreadExecution, etlStatusDTO);
                        } else {
                            finishExecutionThreadForSuccess(etlsThreadExecution, currentThreadExecution);
                        }
                    }

                } else {
                    LOG.info("HOP ETL {} not finished yet", runningEtl.getCode());
                }
            }
        }

        Execution nextExecution = executionService.getOldestInWaitingResultAndEtlExecutionPlatform(TipoPlataformaEjecucion.APACHE_HOP);

        boolean changeWaiting = false;
        List<Long> etlIds = !waitingThreadExecutions.isEmpty() ? waitingThreadExecutions.stream().filter(Objects::nonNull)
                .map(hilo -> hilo.getComputationalThread().getComputationalThreadsEtl()).iterator().next()
                .stream().map(etl -> etl.getEtl().getId()).collect(Collectors.toList()) : new ArrayList<>();
        if (!executionService.existsRunnnigOrWaitingByEtlIdIn(etlIds)) {
            for (ComputationalThreadExecution waitingThreads : waitingThreadExecutions) {
                waitingThreads.setResult(ComputationalThreadExecution.Result.RUNNING);
                waitingThreads.setStartDate(Instant.now());
                this.computationalThreadsService.executeThread(waitingThreads, waitingThreads.getExecutor());
                changeWaiting = true;
            }
        }

        if (nextExecution == null || changeWaiting) {
            LOG.info("There is not HOP ETL to execute.");
            return;
        }

        ComputationalThreadExecutionEtl currentThreadEtlExecution = computationalThreadExecutionService.findByExecutionId(nextExecution.getId());
        if (currentThreadEtlExecution != null && !currentRunningNotFinished.isEmpty()) {
            if (currentRunningNotFinished.stream()
                    .filter(current -> current.getComputationalThreadExecution().getId().equals(currentThreadEtlExecution.getComputationalThreadExecution().getId())).count() > 0) {
                LOG.info("There is not HOP ETL to execute");
                return;
            }
        }

        Etl nextEtl = nextExecution.getEtl();
        final String etlFilename = gitService.getMainFileName(nextEtl);
        WebResultDTO webResultDTO = hopExecutionService.runEtl(nextEtl, etlFilename, nextExecution.getIdExecution());

        Execution nextExecutionResult;
        if (!webResultDTO.isOk()) {
            LOG.error("Error executing next HOP ETL {} - cause: {}", nextEtl.getCode(), webResultDTO.getMessage());
            hopExecutionService.notifyExecutionError(nextEtl);
            runExecuteRemoveEtl(nextEtl, etlFilename, nextExecution);
            nextExecution.setStartDate(Instant.now());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.FAILED, webResultDTO.getMessage());
        } else {
            LOG.info("Executing next HOP etl {}", nextEtl.getCode());
            nextExecutionResult = updateExecutionFromResult(nextExecution, Result.RUNNING);
        }
        executionService.update(nextExecutionResult);
    }
    
    private void runExecuteRemoveEtl(Etl runningEtl, String etlFilename, Execution runningExecution) {
        try {
            hopExecutionService.removeEtl(runningEtl, etlFilename, runningExecution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred removing hop execution ({}): {}", runningEtl.getName(), e.getMessage());
        }
    }

    
    private EtlStatusDTO runExecuteStatusPipeline(String etlFilename, Execution runningExecution, Etl runningEtl) {
        EtlStatusDTO etlStatusDTO;
        try {
            etlStatusDTO = executeStatusPipeline(etlFilename, runningExecution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred checking pipeline execution ({}): {}", runningEtl.getName(), e.getMessage());
            etlStatusDTO = new PipelineStatusDTO();
            etlStatusDTO.setErrorDescription(e.getMessage());
            etlStatusDTO.setStatus(Status.FINISHED_WITH_ERRORS);
        }
        return etlStatusDTO;
    }

    private EtlStatusDTO executeStatusPipeline(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, PipelineMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, PipelineStatusDTO.class).getBody();
    }
    
    private EtlStatusDTO runExecuteStatusWorkflow(String etlFilename, Execution runningExecution, Etl runningEtl) {
        EtlStatusDTO etlStatusDTO;
        try {
            etlStatusDTO = executeStatusWorkflow(etlFilename, runningExecution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred checking workflow execution ({}): {}", runningEtl.getName(), e.getMessage());
            etlStatusDTO = new WorkflowStatusDTO();
            etlStatusDTO.setErrorDescription(e.getMessage());
            etlStatusDTO.setStatus(Status.FINISHED_WITH_ERRORS);
        }
        return etlStatusDTO;
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
            hopExecutionService.notifyExecutionError(currentExecution.getEtl());
            return updateExecutionFromResult(currentExecution, Result.FAILED, etlStatusDTO.getErrorDescription());
        }
        return updateExecutionFromResult(currentExecution, Result.SUCCESS);
    }

    private boolean isExecutionFailed(EtlStatusDTO etlStatusDTO) {
        return (etlStatusDTO.isFinishedWithErrors() || etlStatusDTO.isStoppedWithErrors() || etlStatusDTO.isStopped());
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

    private void finishExecutionThreadForError(List<ComputationalThreadExecutionEtl> etlsThreadExecution, Execution finishedExecution,
            ComputationalThreadExecution currentThreadExecution, EtlStatusDTO etlStatusDTO) {
        for (ComputationalThreadExecutionEtl exe : etlsThreadExecution) {
            if (exe.getExecution().getResult().equals(Result.WAITING) && !exe.getExecution().getId().equals(finishedExecution.getId())) {
                Execution notProccessExecution = exe.getExecution();
                runExecuteRemoveEtl(notProccessExecution.getEtl(), gitService.getMainFileName(notProccessExecution.getEtl()), notProccessExecution);
                notProccessExecution.setResult(Result.FAILED);
                notProccessExecution.setFinishDate(Instant.now());
                notProccessExecution.setStartDate(Instant.now());
                notProccessExecution.setNotes(String.format(MSG_ERROR_EXECUTION_OTHERS_ETL_THREAD, notProccessExecution.getEtl().getName()));
                executionService.update(notProccessExecution);
            }
        }
        computationalThreadExecutionService.setThreadExecutionFailed(currentThreadExecution, etlStatusDTO.getErrorDescription());
        computationalThreadExecutionService.update(currentThreadExecution);
    }

    private void finishExecutionThreadForSuccess(List<ComputationalThreadExecutionEtl> etlsThreadExecution, ComputationalThreadExecution currentThreadExecution) {
        boolean finalizaHilo = true;
        for (ComputationalThreadExecutionEtl exe : etlsThreadExecution) {
            if (exe.getExecution().getResult().equals(Result.WAITING)) {
                finalizaHilo = false;
                break;
            }
        }

        if (finalizaHilo) {
            currentThreadExecution.setResult(ComputationalThreadExecution.Result.SUCCESS);
            currentThreadExecution.setFinishDate(Instant.now());
            computationalThreadExecutionService.update(currentThreadExecution);
        }
    }

}
