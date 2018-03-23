package com.arte.application.template.service;

import java.util.List;

import com.arte.application.template.domain.Idioma;

/**
 * Service Interface for managing Idioma.
 */
public interface IdiomaService {

    /**
     * Save a idioma.
     *
     * @param idioma the entity to save
     * @return the persisted entity
     */
    Idioma save(Idioma idioma);

    /**
     * Get all the idiomas.
     *
     * @return the list of entities
     */
    List<Idioma> findAll();
    
    List<Idioma> findAll(String query);

    /**
     * Get the "id" idioma.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Idioma findOne(Long id);

    /**
     * Delete the "id" idioma.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
