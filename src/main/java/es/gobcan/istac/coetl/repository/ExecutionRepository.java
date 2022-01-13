package es.gobcan.istac.coetl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

    Page<Execution> findAllByEtlId(Long idEtl, Pageable pageable);

    Execution findFirstByEtlId(Long idEtl);

    boolean existsByResultInAndEtlId(List<Result> results, Long idEtl);

    List<Execution> findByResult(Result running);

    Execution findFirstByResultOrderByPlanningDateAsc(Result waiting);
}
