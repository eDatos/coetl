package com.arte.application.template.repository;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arte.application.template.domain.Actor;

/**
 * Spring Data JPA repository for the Actor entity.
 */
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    Page<Actor> findAll(DetachedCriteria criteria, Pageable pageable);
}
