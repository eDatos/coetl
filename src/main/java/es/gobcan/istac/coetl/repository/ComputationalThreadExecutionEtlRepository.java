package es.gobcan.istac.coetl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecutionEtl;

@Repository
public interface ComputationalThreadExecutionEtlRepository extends JpaRepository<ComputationalThreadExecutionEtl, Long> {

    ComputationalThreadExecutionEtl findByExecutionId(Long id);
    List<ComputationalThreadExecutionEtl> findAllByComputationalThreadExecutionId(Long id);

}
