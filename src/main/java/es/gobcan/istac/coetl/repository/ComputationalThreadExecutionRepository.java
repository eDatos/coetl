package es.gobcan.istac.coetl.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;

@Repository
public interface ComputationalThreadExecutionRepository extends JpaRepository<ComputationalThreadExecution, Long> {

    Page<ComputationalThreadExecution> findAll(DetachedCriteria criteria, Pageable pageable);

    Page<ComputationalThreadExecution> findAllByComputationalThreadId(Long idThread, Pageable pageable);

    List<ComputationalThreadExecution> findAllByComputationalThreadIdAndResult(Long idThread, Result result);

    ComputationalThreadExecution findByResultAndId(Result result, Long idExecutionThread);

    List<ComputationalThreadExecution> findByResult(Result result);

    boolean existsByResultAndComputationalThreadId(Result result, Long id);

    @Query(value = "select e.id, e.planning_date, e.start_date, e.finish_date, e.\"type\", e.\"result\", e.notes, e.computational_thread_fk, e.executor "
            + "from computational_threads_executions e where e.computational_thread_fk = ?1 and to_char(e.planning_date, 'DD/MM/YYYY') = ?2 and e.\"result\" = ?3 order by e.id desc limit 1", nativeQuery = true)
    ComputationalThreadExecution findFirstByComputationalThreadIdAndPlanningDateAndResultOrderByIdDesc(Long idThread, String planningExecutionDate, String running);

    ComputationalThreadExecution findFirstByComputationalThreadIdAndResultOrderByIdDesc(Long idThread, Result running);

    @Query(value = "select e.id, e.planning_date, e.start_date, e.finish_date, e.\"type\", e.\"result\", e.notes, e.computational_thread_fk, e.executor from computational_threads_executions e where e.computational_thread_fk = ?1 and to_char(e.planning_date, 'DD/MM/YYYY') = ?2 order by e.id desc limit 1", nativeQuery = true)
    ComputationalThreadExecution findFirstByComputationalThreadIdAndPlanningDateOrderByIdDesc(Long idThread, String planningExecutionDate);

    ComputationalThreadExecution findFirstByComputationalThreadIdOrderByPlanningDateDesc(Long idThread);

}
