package es.gobcan.coetl.service.impl;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.repository.ExecutionRepository;
import es.gobcan.coetl.service.ExecutionService;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    @Autowired
    ExecutionRepository executionRepository;

    @Override
    public Execution create(Execution execution) {
        LOG.debug("Request to create an Execution : {}", execution);
        return save(execution);
    }

    @Override
    public Execution update(Execution execution) {
        LOG.debug("Request to update an Execution : {}", execution);
        execution.setPlanningDate(Instant.now());
        return save(execution);
    }

    @Override
    public Page<Execution> findAllByEtlId(Long idEtl, Pageable pageable) {
        LOG.debug("Request to find a page of all Executions by Etl : {}", idEtl);
        return executionRepository.findAllByEtlId(idEtl, pageable);
    }

    @Override
    public boolean existsRunnnigOrWaitingByEtl(Long idEtl) {
        LOG.debug("Request to get if exists an Execution in RUNNING or WAITING by Etl : {}", idEtl);
        List<Result> results = new LinkedList<>(Arrays.asList(Result.RUNNING, Result.WAITING));
        return executionRepository.existsByResultInAndEtlId(results, idEtl);
    }

    @Override
    public Execution getInRunningResult() {
        LOG.debug("Request to get Execution in RUNNING");
        return executionRepository.findByResult(Result.RUNNING);
    }

    @Override
    public Execution getOldestInWaitingResult() {
        LOG.debug("Request to get the oldest Execution in WAITING");
        return executionRepository.findFirstByResultOrderByPlanningDateAsc(Result.WAITING);
    }

    private Execution save(Execution execution) {
        LOG.debug("Request to create an Execution : {}", execution);
        return executionRepository.saveAndFlush(execution);
    }
}
