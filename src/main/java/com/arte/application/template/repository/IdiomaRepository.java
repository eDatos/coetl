package com.arte.application.template.repository;

import com.arte.application.template.domain.Idioma;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Idioma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdiomaRepository extends JpaRepository<Idioma,Long> {
    
}
