package es.gobcan.istac.coetl.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionRepository;
import es.gobcan.istac.coetl.service.ComputationalThreadExecutionService;

@Service
public class ComputationalThreadExecutionServiceImpl implements ComputationalThreadExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ComputationalThreadExecutionService.class);

    @Autowired
    ComputationalThreadExecutionRepository computationalThreadExecutionRepository;

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
        LOG.debug("Request to find an Execution by Computational Thread Id: {}", idExecutionThread);
        return computationalThreadExecutionRepository.findByResultAndId(result, idExecutionThread);
    }

    @Transactional
    private ComputationalThreadExecution save(ComputationalThreadExecution computationalThreadExecution) {
        LOG.debug("Request to create an Computational Thread Execution : {}", computationalThreadExecution);
        return computationalThreadExecutionRepository.saveAndFlush(computationalThreadExecution);
    }

}
