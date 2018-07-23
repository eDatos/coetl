package es.gobcan.coetl.repository;

import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.Pelicula;

/**
 * Spring Data JPA repository for the Pelicula entity.
 */
@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    public Page<Pelicula> findAll(DetachedCriteria criteria, Pageable pageable);

    public Set<Pelicula> findByActoresId(Long id);
}
