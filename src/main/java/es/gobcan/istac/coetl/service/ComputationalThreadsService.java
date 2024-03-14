package es.gobcan.istac.coetl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.coetl.domain.ComputationalThreads;

public interface ComputationalThreadsService {

    public ComputationalThreads create(ComputationalThreads computationalThreads);
    public ComputationalThreads update(ComputationalThreads computationalThreads);
    public ComputationalThreads findOne(Long id);
    public Page<ComputationalThreads> findAll(String query, boolean includeDeleted, Pageable pageable, String lastExecutionStartDate, String lastExecutionResult);

}
