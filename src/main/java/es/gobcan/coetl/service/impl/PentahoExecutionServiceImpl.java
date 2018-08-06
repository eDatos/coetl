package es.gobcan.coetl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.service.PentahoExecutionService;

@Service
public class PentahoExecutionServiceImpl implements PentahoExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoExecutionServiceImpl.class);

    @Override
    public void execute(String etlCode) {
        LOG.info("Executing ETL : {}", etlCode);
    }
}
