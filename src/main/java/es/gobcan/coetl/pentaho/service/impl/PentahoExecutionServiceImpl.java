package es.gobcan.coetl.pentaho.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import es.gobcan.coetl.config.PentahoProperties;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.pentaho.dto.ServerStatusDTO;
import es.gobcan.coetl.pentaho.enumeration.PentahoServerResource;
import es.gobcan.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.coetl.pentaho.service.util.PentahoUtil;
import es.gobcan.coetl.service.ExecutionService;

@Service
public class PentahoExecutionServiceImpl implements PentahoExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoExecutionServiceImpl.class);

    @Autowired
    PentahoProperties pentahoProperties;

    @Autowired
    ExecutionService executionService;

    @Override
    public void execute(Etl etl, Type type) {
        LOG.info("Executing ETL : {}", etl.getCode());
        executionService.create(etl, type);
        final String url = PentahoUtil.getUrl(pentahoProperties);
        final String user = PentahoUtil.getUser(pentahoProperties);
        final String password = PentahoUtil.getPassword(pentahoProperties);
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        PentahoUtil.execute(user, password, url, PentahoServerResource.STATUS, HttpMethod.GET, queryParams, ServerStatusDTO.class);
    }
}
