package com.arte.application.template.service;

import java.util.List;

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
     * @return the list of entities
     */
    List<Actor> findAll();

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
