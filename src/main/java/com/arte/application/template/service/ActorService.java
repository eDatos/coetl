package com.arte.application.template.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arte.application.template.domain.Actor;

/**
 * Service Interface for managing Actor.
 */
public interface ActorService {

    /**
     * Save a actor.
     *
     * @param actor the entity to save
     * @return the persisted entity
     */
    Actor save(Actor actor);

    /**
     * Get all the actors.
     * 
     * @param query the query filter
     * @param pageable the pagination info
     * @return a page of entities
     */
    Page<Actor> findAll(String quey, Pageable pageable);

    /**
     * Get the "id" actor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Actor findOne(Long id);

    /**
     * Delete the "id" actor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
