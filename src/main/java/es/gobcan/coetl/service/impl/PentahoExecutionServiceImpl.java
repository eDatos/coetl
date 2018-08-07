package es.gobcan.coetl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.service.ExecutionService;
import es.gobcan.coetl.service.PentahoExecutionService;

@Service
public class PentahoExecutionServiceImpl implements PentahoExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoExecutionServiceImpl.class);

    @Autowired
    ExecutionService executionService;

    @Override
    public void execute(Etl etl, Type type) {
        LOG.info("Executing ETL : {}", etl.getCode());
        executionService.create(etl, type);
    }
}
