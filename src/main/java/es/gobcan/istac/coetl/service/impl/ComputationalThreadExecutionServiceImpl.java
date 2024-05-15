package es.gobcan.istac.coetl.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Type;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecutionEtl;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.platform.hop.service.impl.HopExecutionServiceImpl;
import es.gobcan.istac.coetl.platform.hop.service.util.HopUtil;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.WebResultDTO;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionEtlRepository;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionRepository;
import es.gobcan.istac.coetl.service.ComputationalThreadExecutionService;
import es.gobcan.istac.coetl.service.ExecutionService;

@Service
public class ComputationalThreadExecutionServiceImpl implements ComputationalThreadExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ComputationalThreadExecutionService.class);
    private static final String MSG_ERROR_EXECUTION_ETL_THREAD = "Se produjo un error en la ejecución de la ETL: (%s) ";
    private static final String MSG_ERROR_EXECUTION_OTHERS_ETL_THREAD = "No se pudo ejecutar la ETL debido a que otra ETL previa del hilo computacional (%s) falló";

    @Autowired
    ComputationalThreadExecutionRepository computationalThreadExecutionRepository;
    
    @Autowired
    ComputationalThreadExecutionEtlRepository computationalThreadExecutionEtlRepository;
    
    @Autowired
    GitService gitService;
    
    @Autowired
    HopExecutionServiceImpl hopExecutionService;
    
    @Autowired
    private ExecutionService executionService;

    @Override
    public ComputationalThreadExecution create(ComputationalThreadExecution computationalThreadExecution) {
        LOG.debug("Request to create an Computational Thread Execution : {}", computationalThreadExecution);
        return save(computationalThreadExecution);
    }

    @Override
    public ComputationalThreadExecution update(ComputationalThreadExecution computationalThreadExecution) {
        LOG.debug("Request to update an Computational Thread Execution : {}", computationalThreadExecution);
        return save(computationalThreadExecution);
    }

    @Override
    public Page<ComputationalThreadExecution> findAllByComputationalThreadId(Long idThread, Pageable pageable) {
        LOG.debug("Request to find a page of all Executions by Computational Thread Id: {}", idThread);
        return computationalThreadExecutionRepository.findAllByComputationalThreadId(idThread, pageable);
    }

    @Override
    public ComputationalThreadExecution findByResultAndId(Result result, Long idExecutionThread) {
        LOG.debug("Request to find an Execution by Computational Thread Id and result: {} - {}", idExecutionThread, result.toString());
        return computationalThreadExecutionRepository.findByResultAndId(result, idExecutionThread);
    }

    @Override
    public ComputationalThreadExecution findOne(Long id) {
        LOG.debug("Request to find an Execution by Computational Thread Id: {}", id);
        return computationalThreadExecutionRepository.findOne(id);
    }

    @Override
    public List<ComputationalThreadExecution> getThreadsExecutionByResult(Result result) {
        LOG.debug("Request to get Thread Execution by Result {}", result);
        return computationalThreadExecutionRepository.findByResult(result);
    }

    @Override
    public ComputationalThreadExecutionEtl findByExecutionId(Long id) {
        LOG.debug("Request to get Thread Execution Etl by id {}", id);
        return computationalThreadExecutionEtlRepository.findByExecutionId(id);
    }

    @Override
    public List<ComputationalThreadExecutionEtl> findAllByComputationalThreadExecutionId(Long id) {
        LOG.debug("Request to get All Thread Execution Etl by id {}", id);
        return computationalThreadExecutionEtlRepository.findAllByComputationalThreadExecutionId(id);
    }

    @Override
    public boolean existsComputationalThreadExecutionByResultAndId(Result result, Long id) {
        LOG.debug("Request to check if exists a Thread Execution by Result {} and id {}", result, id);
        return computationalThreadExecutionRepository.existsByResultAndComputationalThreadId(result, id);
    }

    @Transactional
    private ComputationalThreadExecution save(ComputationalThreadExecution computationalThreadExecution) {
        LOG.debug("Request to create an Computational Thread Execution : {}", computationalThreadExecution);
        return computationalThreadExecutionRepository.saveAndFlush(computationalThreadExecution);
    }

    // EXECUTION THREAD
    private Execution registerExecutionEtl(Etl etl, String executor) {
        gitService.updateRepository(etl);
        WebResultDTO webResultDTO = hopExecutionService.registerETL(etl);

        if (!webResultDTO.isOk()) {
            return HopUtil.buildExecution(etl, Execution.Type.MANUAL, executor, Execution.Result.FAILED, null, webResultDTO.getMessage());
        }
        String idExecution = webResultDTO.getId();
        return HopUtil.buildExecution(etl, Execution.Type.MANUAL, executor, Execution.Result.WAITING, idExecution, webResultDTO.getMessage());
    }

    private void unRegisterExecutionEtl(Execution execution) {
        final String etlFilename = gitService.getMainFileName(execution.getEtl());
        try {
            hopExecutionService.removeEtl(execution.getEtl(), etlFilename, execution.getIdExecution());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred removing hop execution ({}): {}", execution.getEtl().getName(), e.getMessage());
        }
    }

    @Override
    public List<Execution> registerHopETL(List<Etl> etls, String executor) {
        List<Execution> registros = new ArrayList<>();
        registros.addAll(etls.stream().map(etl -> registerExecutionEtl(etl, executor)).collect(Collectors.toList()));
        return registros;
    }
    
    @Override
    public void unRegisterHopETL(List<Execution> executions) {
        executions.stream().forEach(execution -> unRegisterExecutionEtl(execution));
    }

    @Override
    public Execution runHopETL(Etl etl, Execution execution) {
        final String etlFilename = gitService.getMainFileName(etl);
        WebResultDTO webResultDTO = hopExecutionService.runEtl(etl, etlFilename, execution.getIdExecution());
        Execution nextExecutionResult;
        if (!webResultDTO.isOk()) {
            LOG.error("Error executing next HOP ETL {} - cause: {}", etl.getCode(), webResultDTO.getMessage());
            //hopExecutionService.notifyExecutionError(etl);
            execution.setStartDate(Instant.now());
            nextExecutionResult = updateExecutionFromResult(execution, Execution.Result.FAILED, webResultDTO.getMessage());
        } else {
            LOG.info("Executing next HOP etl {}", etl.getCode());
            nextExecutionResult = updateExecutionFromResult(execution, Execution.Result.RUNNING);
        }
        return nextExecutionResult;
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Execution.Result result, String notes) {
        if (Execution.Result.RUNNING.equals(result)) {
            currentExecution.setStartDate(Instant.now());
        }
        if (Execution.Result.FAILED.equals(result) || Execution.Result.SUCCESS.equals(result)) {
            currentExecution.setFinishDate(Instant.now());
        }
        currentExecution.setResult(result);
        currentExecution.setNotes(notes);
        return currentExecution;
    }

    private Execution updateExecutionFromResult(Execution currentExecution, Execution.Result result) {
        return updateExecutionFromResult(currentExecution, result, null);
    }

    private void setAllETLExecutionsFailed(List<Execution> registerExecutions, Execution nextExecutionResult) {
        for (Execution resultExecution : registerExecutions) {
            if (!nextExecutionResult.getId().equals(resultExecution.getId())) {
                resultExecution.setNotes(String.format(MSG_ERROR_EXECUTION_OTHERS_ETL_THREAD, nextExecutionResult.getEtl().getName()));
                resultExecution.setStartDate(Instant.now());
            }
            resultExecution.setFinishDate(Instant.now());
            resultExecution.setResult(Execution.Result.FAILED);
            executionService.update(resultExecution);
        }
    }

    @Override
    public void setThreadExecutionFailed(ComputationalThreadExecution computationalThreadExecution, String notes) {
        computationalThreadExecution.setResult(Result.FAILED);
        computationalThreadExecution.setFinishDate(Instant.now());
        computationalThreadExecution.setNotes(notes);
    }

    @Override
    public void executeFirstEtlInThread(List<Execution> registerExecutions, ComputationalThreadExecution computationalThreadExecution) {
        Etl executionFirstEtl = computationalThreadExecution.getComputationalThread().getComputationalThreadsEtl()
                .stream().filter(comp -> comp.getExecutionOrder() == 0).findFirst().get().getEtl();
        if (executionService.existsRunnnigOrWaitingByEtl(executionFirstEtl.getId())) {
            Execution firstExecution = registerExecutions.stream().filter(ex -> ex.getEtl().getId().equals(executionFirstEtl.getId())).findFirst().get();
            Execution nextExecutionResult = runHopETL(executionFirstEtl, firstExecution);
            if (nextExecutionResult.getResult().equals(Execution.Result.FAILED)) {
                unRegisterHopETL(registerExecutions);
                setAllETLExecutionsFailed(registerExecutions, nextExecutionResult);
                String baseErrorMsg = String.format(MSG_ERROR_EXECUTION_ETL_THREAD, nextExecutionResult.getEtl().getName());
                String msgError = StringUtils.substring(baseErrorMsg.concat("\n").concat(nextExecutionResult.getNotes()), 0, 4000);
                setThreadExecutionFailed(computationalThreadExecution, msgError);
            } else {
                executionService.update(nextExecutionResult);
            }
        }
    }

    @Override
    public boolean createAllThreadETLExecutions(ComputationalThreadExecution computationalThreadExecution, List<Execution> registerExecutions) {
        computationalThreadExecution.setComputationalThreadExecutionEtl(new ArrayList<ComputationalThreadExecutionEtl>());
        for (Execution resultExecution : registerExecutions) {
            try {
                Execution created = executionService.create(resultExecution);
                ComputationalThreadExecutionEtl threadExecution = new ComputationalThreadExecutionEtl();
                threadExecution.setComputationalThreadExecution(computationalThreadExecution);
                threadExecution.setExecution(created);
                computationalThreadExecution.getComputationalThreadExecutionEtl().add(threadExecution);
            } catch (Exception e) {
                LOG.error("An unexpected error occurred creating ETL execution ({}): {}", resultExecution.getEtl().getName(), e.getMessage());
                return true;
            }
        }
        return false;
    }

    @Override
    public ComputationalThreadExecution initDefaultExecutionCronJob() {
        ComputationalThreadExecution newExecution = new ComputationalThreadExecution();
        newExecution.setType(Type.AUTO);
        newExecution.setResult(Result.RUNNING);
        return newExecution;
    }

}
