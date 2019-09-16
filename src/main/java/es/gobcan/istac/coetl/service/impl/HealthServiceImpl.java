package es.gobcan.istac.coetl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import es.gobcan.istac.coetl.domain.Health;
import es.gobcan.istac.coetl.repository.HealthRepository;
import es.gobcan.istac.coetl.service.HealthService;
import es.gobcan.istac.coetl.service.validator.HealthValidator;

@Service
public class HealthServiceImpl implements HealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthService.class);

    private enum Status {
        UP, DOWN
    }

    private static class CheckHealthDetail {

        private final Status status;
        private final String endpoint;
        private final Long id;
        private final boolean custom = true;

        public CheckHealthDetail(Status status, String endpoint, Long id) {
            this.status = status;
            this.endpoint = endpoint;
            this.id = id;
        }

        public Status getStatus() {
            return status;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public Long getId() {
            return id;
        }

        public boolean isCustom() {
            return custom;
        }
    }

    @Autowired
    private HealthRepository healthRepository;

    @Autowired
    private HealthValidator healthValidator;

    @Override
    public Health create(Health health) {
        LOGGER.debug("Request to create a Health : {}", health);
        healthValidator.validate(health);
        return save(health);
    }

    @Override
    public Health update(Health health) {
        LOGGER.debug("Request to update a Health : {}", health);
        healthValidator.validate(health);
        return save(health);
    }

    @Override
    public void delete(Health health) {
        LOGGER.debug("Request to delete a Health : {}", health);
        healthRepository.delete(health);
    }

    @Override
    public Health findOne(Long id) {
        LOGGER.debug("Request to get a Health : {}", id);
        return healthRepository.findOne(id);
    }

    @Override
    public List<Health> findAll() {
        LOGGER.debug("Request to find all Healths");
        return healthRepository.findAll();
    }

    @Override
    public Map<String, Object> check() {
        LOGGER.debug("Request to check all Healths");
        Map<String, Object> checkHealth = new HashMap<>();
        checkHealth.put("status", Status.UP);

        List<Health> healths = findAll();
        if (CollectionUtils.isEmpty(healths)) {
            return checkHealth;
        }

        for (Health health : healths) {
            Status status = getHealthStatus(health);
            if (Status.DOWN.equals(status)) {
                checkHealth.put("status", Status.DOWN);
            }
            CheckHealthDetail healthDetail = new CheckHealthDetail(status, health.getEndpoint(), health.getId());
            checkHealth.put(health.getServiceName(), healthDetail);
        }

        return checkHealth;
    }

    private Health save(Health health) {
        LOGGER.debug("Request to save a Health : {}", health);
        return healthRepository.saveAndFlush(health);
    }

    private Status getHealthStatus(Health health) {
        try {
            HttpStatus httpStatus = getEndpointStatus(health.getEndpoint());
            if (!httpStatus.is2xxSuccessful()) {
                return Status.DOWN;
            }
        } catch (Exception e) {
            LOGGER.warn(String.format("%s not available. Impossible to reach endpoint: %s", health.getServiceName(), health.getEndpoint()), e);
            return Status.DOWN;
        }

        return Status.UP;
    }

    private HttpStatus getEndpointStatus(String endpoint) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(endpoint, HttpMethod.GET, null, Void.class);
        return response.getStatusCode();
    }
}