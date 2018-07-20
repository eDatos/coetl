package com.arte.application.template.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.arte.application.template.domain.Idioma;
import com.arte.application.template.repository.IdiomaRepository;
import com.arte.application.template.service.IdiomaService;
import com.arte.application.template.web.rest.util.QueryUtil;

/**
 * Service Implementation for managing Idioma.
 */
@Service
public class IdiomaServiceImpl implements IdiomaService {

    private final Logger log = LoggerFactory.getLogger(IdiomaServiceImpl.class);

    private final IdiomaRepository idiomaRepository;

    private QueryUtil queryUtil;

    public IdiomaServiceImpl(IdiomaRepository idiomaRepository, QueryUtil queryUtil) {
        this.idiomaRepository = idiomaRepository;
        this.queryUtil = queryUtil;
    }

    /**
     * Save a idioma.
     *
     * @param idioma the entity to save
     * @return the persisted entity
     */
    @Override
    public Idioma save(Idioma idioma) {
        log.debug("Request to save Idioma : {}", idioma);
        return idiomaRepository.saveAndFlush(idioma);
    }

    /**
     * Get all the idiomas.
     *
     * @return the list of entities
     */
    @Override
    public List<Idioma> findAll() {
        log.debug("Request to get all Idiomas");
        return idiomaRepository.findAll();
    }

    @Override
    public List<Idioma> findAll(String query) {
        log.debug("Petición para buscar idiomas con query {}", query);
        DetachedCriteria criteria = queryUtil.queryToIdiomaCriteria(null, query);
        return idiomaRepository.findAll(criteria);
    }

    /**
     * Get one idioma by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Idioma findOne(Long id) {
        log.debug("Request to get Idioma : {}", id);
        return idiomaRepository.findOne(id);
    }

    /**
     * Delete the idioma by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Idioma : {}", id);
        idiomaRepository.delete(id);
    }
}
