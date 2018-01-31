package com.arte.application.template.config.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.arte.application.template.config.ApplicationProperties;

@Component
public class CasHealthIndicator extends AbstractHealthIndicator {

    private final Logger logger = LoggerFactory.getLogger(CasHealthIndicator.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        final String casEndPoint = applicationProperties.getCas().getEndpoint();
        builder.withDetail("endpoint", casEndPoint);
        if (checkUrlStatus(casEndPoint) == HttpStatus.ACCEPTED || checkUrlStatus(casEndPoint) == HttpStatus.OK) {
            builder.up();
        } else {
            logger.warn("Cas not available. " + "Impossible to reach");
            builder.down();
        }
    }

    public final HttpStatus checkUrlStatus(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = restTemplate.getForEntity(url, String.class);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getStatusCode();
    }
}
