package es.gobcan.coetl.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.Idioma;

/**
 * Spring Data JPA repository for the Idioma entity.
 */
@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Long> {

    List<Idioma> findAll(DetachedCriteria criteria);
}
