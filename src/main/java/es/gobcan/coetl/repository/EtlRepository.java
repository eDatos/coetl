package es.gobcan.coetl.repository;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.Etl;

@Repository
public interface EtlRepository extends JpaRepository<Etl, Long> {

    Page<Etl> findAll(DetachedCriteria criteria, Pageable pageable);

    Etl findOneByCode(String code);
}
