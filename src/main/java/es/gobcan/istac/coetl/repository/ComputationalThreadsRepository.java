package es.gobcan.istac.coetl.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ComputationalThreads;

@Repository
public interface ComputationalThreadsRepository extends JpaRepository<ComputationalThreads, Long> {

    ComputationalThreads findOneByCode(String code);
    Page<ComputationalThreads> findAll(DetachedCriteria criteria, Pageable pageable);
    List<ComputationalThreads> findAllByComputationalThreadsEtlEtlId(Long idEtl);

}
