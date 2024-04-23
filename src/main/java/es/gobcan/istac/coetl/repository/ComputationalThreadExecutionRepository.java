package es.gobcan.istac.coetl.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;

@Repository
public interface ComputationalThreadExecutionRepository extends JpaRepository<ComputationalThreadExecution, Long> {

    Page<ComputationalThreadExecution> findAll(DetachedCriteria criteria, Pageable pageable);
    Page<ComputationalThreadExecution> findAllByComputationalThreadId(Long idThread, Pageable pageable);
    List<ComputationalThreadExecution> findAllByComputationalThreadIdAndResult(Long idThread, Result result);
    ComputationalThreadExecution findByResultAndId(Result result, Long idExecutionThread);

}
