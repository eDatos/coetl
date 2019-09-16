package es.gobcan.istac.coetl.service;

import java.util.List;
import java.util.Map;

import es.gobcan.istac.coetl.domain.Health;

public interface HealthService {

    public Health create(Health health);
    public Health update(Health health);
    public void delete(Health health);
    public Health findOne(Long id);
    public List<Health> findAll();
    public Map<String, Object> check();
}