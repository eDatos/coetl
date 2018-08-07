package es.gobcan.coetl.service.impl;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.repository.ExecutionRepository;
import es.gobcan.coetl.service.ExecutionService;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    @Autowired
    ExecutionRepository executionRepository;

    @Override
    public Execution create(Etl etl, Type type) {
        LOG.debug("Request to create an Execution ({}) from ETL : {}", type, etl);
        Execution execution = new Execution();
        execution.setDatetime(ZonedDateTime.now());
        execution.setType(type);
        execution.setResult(Result.SUCCESS);
        execution.setEtl(etl);
        return save(execution);
    }

    @Override
    public Page<Execution> findAllByEtlId(Long idEtl, Pageable pageable) {
        LOG.debug("Request to find a page of all Executions by Etl : {}", idEtl);
        return executionRepository.findAllByEtlId(idEtl, pageable);
    }

    private Execution save(Execution execution) {
        LOG.debug("Request to create an Execution : {}", execution);
        return executionRepository.save(execution);
    }
}
