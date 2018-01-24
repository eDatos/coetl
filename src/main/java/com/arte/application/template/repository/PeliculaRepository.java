package com.arte.application.template.repository;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arte.application.template.domain.Pelicula;

/**
 * Spring Data JPA repository for the Pelicula entity.
 */
@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    public Page<Pelicula> findAll(DetachedCriteria criteria, Pageable pageable);
}
