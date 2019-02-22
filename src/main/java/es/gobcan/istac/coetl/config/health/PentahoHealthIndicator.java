package es.gobcan.istac.coetl.config.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import es.gobcan.istac.coetl.config.PentahoProperties;
import es.gobcan.istac.coetl.pentaho.enumeration.ServerMethodsEnum;
import es.gobcan.istac.coetl.pentaho.service.util.PentahoUtil;
import es.gobcan.istac.coetl.pentaho.web.rest.dto.ServerStatusDTO;

@Component
public class PentahoHealthIndicator extends AbstractHealthIndicator {

    private final Logger logger = LoggerFactory.getLogger(PentahoHealthIndicator.class);

    @Autowired
    private PentahoProperties pentahoProperties;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        final String pentahoUrl = PentahoUtil.getUrl(pentahoProperties);
        builder.withDetail("endpoint", pentahoUrl);
        ResponseEntity<ServerStatusDTO> response = executePentahoUrl(pentahoUrl);
        if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody().isOnline()) {
            builder.up();
        } else {
            logger.warn("Pentaho Server Data Integration not available");
            builder.down();
        }
    }

    public final ResponseEntity<ServerStatusDTO> executePentahoUrl(String url) {
        final String user = PentahoUtil.getUser(pentahoProperties);
        final String password = PentahoUtil.getPassword(pentahoProperties);
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, ServerMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, ServerStatusDTO.class);
    }
}
