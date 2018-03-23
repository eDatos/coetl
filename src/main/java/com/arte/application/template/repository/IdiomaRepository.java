package com.arte.application.template.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arte.application.template.domain.Idioma;


/**
 * Spring Data JPA repository for the Idioma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdiomaRepository extends JpaRepository<Idioma,Long> {
 
    List<Idioma> findAll(DetachedCriteria criteria);
}
