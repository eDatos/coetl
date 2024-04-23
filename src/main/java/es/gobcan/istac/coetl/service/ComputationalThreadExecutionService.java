package es.gobcan.istac.coetl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;

public interface ComputationalThreadExecutionService {

    public ComputationalThreadExecution create(ComputationalThreadExecution execution);
    public ComputationalThreadExecution update(ComputationalThreadExecution execution);
    public Page<ComputationalThreadExecution> findAllByComputationalThreadId(Long idThread, Pageable pageable);
    public ComputationalThreadExecution findByResultAndId(Result result, Long idExecutionThread);

}
