package com.arte.application.template.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.domain.Pelicula;
import com.arte.application.template.repository.PeliculaRepository;
import com.arte.application.template.service.PeliculaService;

/**
 * Service Implementation for managing Pelicula.
 */
@Service
@Transactional
public class PeliculaServiceImpl implements PeliculaService {

    private final Logger log = LoggerFactory.getLogger(PeliculaServiceImpl.class);

    private final PeliculaRepository peliculaRepository;

    public PeliculaServiceImpl(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
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
        return peliculaRepository.save(pelicula);
    }

    /**
     * Get all the peliculas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pelicula> findAll(Pageable pageable) {
        log.debug("Request to get all Peliculas");
        return peliculaRepository.findAll(pageable);
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
}
