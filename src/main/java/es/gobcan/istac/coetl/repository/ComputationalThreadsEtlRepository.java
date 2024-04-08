package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ComputationalThreadsEtl;

@Repository
public interface ComputationalThreadsEtlRepository extends JpaRepository<ComputationalThreadsEtl, Long> {

}
