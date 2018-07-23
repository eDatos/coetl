package es.gobcan.coetl.service;

import java.util.List;
import java.util.Set;

import es.gobcan.coetl.domain.Categoria;

/**
 * Service Interface for managing Categoria.
 */
public interface CategoriaService {

    /**
     * Save a categoria.
     *
     * @param categoria the entity to save
     * @return the persisted entity
     */
    Categoria save(Categoria categoria);

    /**
     * Get all the categorias.
     *
     * @return the list of entities
     */
    List<Categoria> findAll();

    /**
     * Get the "id" categoria.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Categoria findOne(Long id);

    /**
     * Delete the "id" categoria.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Save a set of categorias.
     *
     * @param categorias the set of entities to save
     * @return the set of persisted entities
     */
    Set<Categoria> save(Set<Categoria> categorias);
}
