package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.Health;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {

}