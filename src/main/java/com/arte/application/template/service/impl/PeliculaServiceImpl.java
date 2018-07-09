package com.arte.application.template.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.domain.Documento;
import com.arte.application.template.domain.Pelicula;
import com.arte.application.template.repository.PeliculaRepository;
import com.arte.application.template.service.CategoriaService;
import com.arte.application.template.service.PeliculaService;
import com.arte.application.template.web.rest.util.QueryUtil;

/**
 * Service Implementation for managing Pelicula.
 */
@Service
public class PeliculaServiceImpl implements PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaServiceImpl.class);

    private final PeliculaRepository peliculaRepository;
    private final CategoriaService categoriaService;
    private final QueryUtil queryUtil;

    public PeliculaServiceImpl(PeliculaRepository peliculaRepository, CategoriaService categoriaService, QueryUtil queryUtil) {
        this.peliculaRepository = peliculaRepository;
        this.categoriaService = categoriaService;
        this.queryUtil = queryUtil;
    }

    /**
     * Save a pelicula.
     *
     * @param pelicula the entity to save
     * @return the persisted entity
     */
    @Override
    public Pelicula save(Pelicula pelicula) {
        log.debug("Request to save Pelicula : {}", pelicula);
        saveCategorias(pelicula);
        return peliculaRepository.saveAndFlush(pelicula);
    }

    /**
     * Get all the peliculas.
     *
     * @param query the query filter
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pelicula> findAll(String query, Pageable pageable) {
        log.debug("Request to get all Peliculas");
        DetachedCriteria criteria = queryUtil.queryToPeliculaCriteria(pageable, query);
        return peliculaRepository.findAll(criteria, pageable);
    }

    /**
     * Get one pelicula by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Pelicula findOne(Long id) {
        log.debug("Request to get Pelicula : {}", id);
        return peliculaRepository.findOne(id);
    }

    /**
     * Delete the pelicula by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pelicula : {}", id);
        peliculaRepository.delete(id);
    }

    /**
     * Bind a documento to pelicula
     * 
     * @param pelicula the entity to update
     * @param documento the documento to bind
     * @return the persisted entity
     */
    @Override
    public Pelicula bindDocumento(Pelicula pelicula, Documento documento) {
        log.debug("request to bind a Documento to Pelicula : {}", pelicula);
        pelicula.setDocumento(documento);
        return peliculaRepository.saveAndFlush(pelicula);
    }

    private void saveCategorias(Pelicula pelicula) {
        if (!pelicula.getCategorias().isEmpty()) {
            pelicula.setAllCategorias(categoriaService.save(pelicula.getCategorias()));
        }
    }
}
