package com.arte.application.template.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arte.application.template.domain.Idioma;
import com.arte.application.template.service.IdiomaService;
import com.arte.application.template.web.rest.dto.IdiomaDTO;
import com.arte.application.template.web.rest.mapper.IdiomaMapper;
import com.arte.application.template.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Idioma.
 */
@RestController
@RequestMapping("/api/idiomas")
public class IdiomaResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(IdiomaResource.class);

    private static final String ENTITY_NAME = "idioma";
    private static final String BASE_URL = "/api/idiomas";

    private final IdiomaService idiomaService;

    private final IdiomaMapper idiomaMapper;

    public IdiomaResource(IdiomaService idiomaService, IdiomaMapper idiomaMapper) {
        this.idiomaService = idiomaService;
        this.idiomaMapper = idiomaMapper;
    }

    /**
     * POST /idiomas : Create a new idioma.
     *
     * @param idiomaDTO the idiomaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new idiomaDTO, or with status 400 (Bad Request) if the idioma has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping
    @Timed
    public ResponseEntity<IdiomaDTO> createIdioma(@Valid @RequestBody IdiomaDTO idiomaDTO) throws URISyntaxException {
        log.debug("REST request to save Idioma : {}", idiomaDTO);
        if (idiomaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new idioma cannot already have an ID")).body(null);
        }
        Idioma idioma = idiomaMapper.toEntity(idiomaDTO);
        IdiomaDTO result = idiomaMapper.toDto(idiomaService.save(idioma));
        return ResponseEntity.created(new URI(BASE_URL + "/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /idiomas : Updates an existing idioma.
     *
     * @param idiomaDTO the idiomaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated idiomaDTO,
     *         or with status 400 (Bad Request) if the idiomaDTO is not valid,
     *         or with status 500 (Internal Server Error) if the idiomaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @Timed
    public ResponseEntity<IdiomaDTO> updateIdioma(@Valid @RequestBody IdiomaDTO idiomaDTO) throws URISyntaxException {
        log.debug("REST request to update Idioma : {}", idiomaDTO);
        if (idiomaDTO.getId() == null) {
            return createIdioma(idiomaDTO);
        }
        Idioma idioma = idiomaMapper.toEntity(idiomaDTO);
        IdiomaDTO result = idiomaMapper.toDto(idiomaService.save(idioma));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, idiomaDTO.getId().toString())).body(result);
    }

    /**
     * GET /idiomas : get all the idiomas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of idiomas in body
     */
    @GetMapping
    @Timed
    public List<IdiomaDTO> getAllIdiomas() {
        log.debug("REST request to get all Idiomas");
        return idiomaMapper.toDto(idiomaService.findAll());
    }

    /**
     * GET /idiomas/:id : get the "id" idioma.
     *
     * @param id the id of the idiomaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the idiomaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<IdiomaDTO> getIdioma(@PathVariable Long id) {
        log.debug("REST request to get Idioma : {}", id);
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idiomaService.findOne(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(idiomaDTO));
    }

    /**
     * DELETE /idiomas/:id : delete the "id" idioma.
     *
     * @param id the id of the idiomaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @Timed
    public ResponseEntity<Void> deleteIdioma(@PathVariable Long id) {
        log.debug("REST request to delete Idioma : {}", id);
        idiomaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
