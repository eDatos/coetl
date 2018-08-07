package es.gobcan.coetl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.Execution;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

    Page<Execution> findAllByEtlId(Long idEtl, Pageable pageable);
}
