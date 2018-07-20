package com.arte.application.template.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arte.application.template.domain.Categoria;
import com.arte.application.template.errors.ErrorConstants;
import com.arte.application.template.service.CategoriaService;
import com.arte.application.template.web.rest.dto.CategoriaDTO;
import com.arte.application.template.web.rest.mapper.CategoriaMapper;
import com.arte.application.template.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Categoria.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);

    private static final String ENTITY_NAME = "categoria";
    private static final String BASE_URL = "/api/categorias";

    private final CategoriaService categoriaService;

    private final CategoriaMapper categoriaMapper;

    public CategoriaResource(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    /**
     * POST /categorias : Create a new categoria.
     *
     * @param categoriaDTO the categoriaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categoriaDTO, or with status 400 (Bad Request) if the categoria has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.puedeCrearCategoria(authentication)")
    public ResponseEntity<CategoriaDTO> createCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoriaDTO);
        if (categoriaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new categoria cannot already have an ID")).body(null);
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        CategoriaDTO result = categoriaMapper.toDto(categoriaService.save(categoria));
        return ResponseEntity.created(new URI(BASE_URL + "/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /categorias : Updates an existing categoria.
     *
     * @param categoriaDTO the categoriaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categoriaDTO,
     *         or with status 400 (Bad Request) if the categoriaDTO is not valid,
     *         or with status 500 (Internal Server Error) if the categoriaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.puedeModificarCategoria(authentication)")
    public ResponseEntity<CategoriaDTO> updateCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}", categoriaDTO);
        if (categoriaDTO.getId() == null) {
            return createCategoria(categoriaDTO);
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        CategoriaDTO result = categoriaMapper.toDto(categoriaService.save(categoria));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categoriaDTO.getId().toString())).body(result);
    }

    /**
     * GET : get all the categorias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categorias in body
     */
    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarCategoria(authentication)")
    public List<CategoriaDTO> getAllCategorias() {
        log.debug("REST request to get all Categorias");
        return categoriaMapper.toDto(categoriaService.findAll());
    }

    /**
     * GET /categorias/:id : get the "id" categoria.
     *
     * @param id the id of the categoriaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categoriaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarCategoria(authentication)")
    public ResponseEntity<CategoriaDTO> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        CategoriaDTO categoriaDTO = categoriaMapper.toDto(categoriaService.findOne(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(categoriaDTO));
    }

    /**
     * DELETE /categorias/:id : delete the "id" categoria.
     *
     * @param id the id of the categoriaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.puedeBorrarCategoria(authentication)")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);
        categoriaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
