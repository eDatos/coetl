package es.gobcan.istac.coetl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecutionEtl;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;

public interface ComputationalThreadExecutionService {

    public ComputationalThreadExecution create(ComputationalThreadExecution execution);
    public ComputationalThreadExecution update(ComputationalThreadExecution execution);
    public Page<ComputationalThreadExecution> findAllByComputationalThreadId(Long idThread, Pageable pageable);
    public ComputationalThreadExecution findByResultAndId(Result result, Long idExecutionThread);
    public List<Execution> registerHopETL(List<Etl> etls, String executor);
    public boolean createAllThreadETLExecutions(ComputationalThreadExecution computationalThreadExecution, List<Execution> registerExecutions);
    public void unRegisterHopETL(List<Execution> executions);
    public Execution runHopETL(Etl etl, Execution execution);
    public void executeFirstEtlInThread(List<Execution> registerExecutions, ComputationalThreadExecution computationalThreadExecution);
    public void setThreadExecutionFailed(ComputationalThreadExecution computationalThreadExecution, String notes);
    public List<ComputationalThreadExecution> getThreadsExecutionByResult(Result result);
    public ComputationalThreadExecutionEtl findByExecutionId(Long id);
    public List<ComputationalThreadExecutionEtl> findAllByComputationalThreadExecutionId(Long id);
    public ComputationalThreadExecution findOne(Long id);
    public boolean existsComputationalThreadExecutionByResultAndId(Result result, Long id);
    public ComputationalThreadExecution initDefaultExecutionCronJob();

}
