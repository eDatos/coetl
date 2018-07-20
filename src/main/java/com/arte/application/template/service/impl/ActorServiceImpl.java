package com.arte.application.template.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.arte.application.template.domain.Actor;
import com.arte.application.template.domain.Documento;
import com.arte.application.template.repository.ActorRepository;
import com.arte.application.template.repository.PeliculaRepository;
import com.arte.application.template.service.ActorService;
import com.arte.application.template.service.DocumentoService;
import com.arte.application.template.web.rest.util.QueryUtil;

/**
 * Service Implementation for managing Actor.
 */
@Service
public class ActorServiceImpl implements ActorService {

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    private final PeliculaRepository peliculaRepository;

    private final QueryUtil queryUtil;

    private final DocumentoService documentoService;

    public ActorServiceImpl(ActorRepository actorRepository, QueryUtil queryUtil, PeliculaRepository peliculaRepository, DocumentoService documentoService) {
        this.actorRepository = actorRepository;
        this.queryUtil = queryUtil;
        this.peliculaRepository = peliculaRepository;
        this.documentoService = documentoService;
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
        return actorRepository.saveAndFlush(actor);
    }

    /**
     * Get all the actors.
     *
     * @return the list of entities
     */
    @Override
    public Page<Actor> findAll(String query, Pageable pageable) {
        log.debug("Request to get all Actors");
        DetachedCriteria criteria = queryUtil.queryToActorCriteria(pageable, query);
        return actorRepository.findAll(criteria, pageable);
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
        log.debug("Request to delete Actor");
        Actor actor = actorRepository.findOne(id);
        actor.setPeliculas(peliculaRepository.findByActoresId(actor.getId()));
        actor.removeAllPeliculas();
        actorRepository.delete(actor);
    }

    /**
     * Bind a documento to actor
     * 
     * @param actor the entity to update
     * @param documento the documento to bind
     * @return the persisted entity
     */
    @Override
    public Actor bindDocumento(Actor actor, Documento documento) {
        actor.addDocumento(documento);
        return save(actor);
    }

    /**
     * Unbind a documento to actor
     * 
     * @param actor the entity to update
     * @param documento the documento to unbind
     * @return the persisted entity
     */
    @Override
    public Actor unbindDocumento(Actor actor, Documento documento) {
        actor.removeDocumento(documento);
        documentoService.delete(documento.getId());
        return save(actor);
    }
}
