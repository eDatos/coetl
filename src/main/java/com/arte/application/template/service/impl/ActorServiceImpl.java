package com.arte.application.template.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.arte.application.template.domain.Actor;
import com.arte.application.template.repository.ActorRepository;
import com.arte.application.template.service.ActorService;

/**
 * Service Implementation for managing Actor.
 */
@Service
public class ActorServiceImpl implements ActorService {

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    /**
     * Save a actor.
     *
     * @param actor the entity to save
     * @return the persisted entity
     */
    @Override
    public Actor save(Actor actor) {
        log.debug("Request to save Actor : {}", actor);
        return actorRepository.save(actor);
    }

    /**
     * Get all the actors.
     *
     * @return the list of entities
     */
    @Override
    public List<Actor> findAll() {
        log.debug("Request to get all Actors");
        return actorRepository.findAll();
    }

    /**
     * Get one actor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Actor findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        return actorRepository.findOne(id);
    }

    /**
     * Delete the actor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        actorRepository.delete(id);
    }
}
