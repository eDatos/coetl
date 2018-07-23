package es.gobcan.coetl.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.domain.Categoria;
import es.gobcan.coetl.repository.CategoriaRepository;
import es.gobcan.coetl.service.CategoriaService;

/**
 * Service Implementation for managing Categoria.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Save a categoria.
     *
     * @param categoria the entity to save
     * @return the persisted entity
     */
    @Override
    public Categoria save(Categoria categoria) {
        log.debug("Request to save Categoria : {}", categoria);
        return categoriaRepository.saveAndFlush(categoria);
    }

    /**
     * Get all the categorias.
     *
     * @return the list of entities
     */
    @Override
    public List<Categoria> findAll() {
        log.debug("Request to get all Categorias");
        return categoriaRepository.findAll();
    }

    /**
     * Get one categoria by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Categoria findOne(Long id) {
        log.debug("Request to get Categoria : {}", id);
        return categoriaRepository.findOne(id);
    }

    /**
     * Delete the categoria by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categoria : {}", id);
        categoriaRepository.delete(id);
    }

    /**
     * Save a set of categorias.
     *
     * @param categorias the set of entities to save
     * @return the set of persisted entities
     */
    public Set<Categoria> save(Set<Categoria> categorias) {
        Set<Categoria> set = new HashSet<>();
        categorias.forEach(categoria -> set.add(categoriaRepository.saveAndFlush(categoria)));
        return set;
    }
}
