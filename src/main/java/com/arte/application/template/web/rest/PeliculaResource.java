package com.arte.application.template.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arte.application.template.domain.Pelicula;
import com.arte.application.template.service.PeliculaService;
import com.arte.application.template.web.rest.dto.PeliculaDTO;
import com.arte.application.template.web.rest.mapper.PeliculaMapper;
import com.arte.application.template.web.rest.util.HeaderUtil;
import com.arte.application.template.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Pelicula.
 */
@RestController
@RequestMapping("/api/peliculas")
public class PeliculaResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(PeliculaResource.class);

    private static final String ENTITY_NAME = "pelicula";
    private static final String BASE_URL = "/api/peliculas";

    private final PeliculaService peliculaService;

    private final PeliculaMapper peliculaMapper;

    public PeliculaResource(PeliculaService peliculaService, PeliculaMapper peliculaMapper) {
        this.peliculaService = peliculaService;
        this.peliculaMapper = peliculaMapper;
    }

    /**
     * POST : Create a new pelicula.
     *
     * @param peliculaDTO the peliculaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new peliculaDTO, or with status 400 (Bad Request) if the pelicula has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping
    @Timed
    public ResponseEntity<PeliculaDTO> createPelicula(@Valid @RequestBody PeliculaDTO peliculaDTO) throws URISyntaxException {
        log.debug("REST request to save Pelicula : {}", peliculaDTO);
        if (peliculaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pelicula cannot already have an ID")).body(null);
        }
        Pelicula pelicula = peliculaMapper.toEntity(peliculaDTO);
        PeliculaDTO result = peliculaMapper.toDto(peliculaService.save(pelicula));
        return ResponseEntity.created(new URI(BASE_URL + "/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT : Updates an existing pelicula.
     *
     * @param peliculaDTO the peliculaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated peliculaDTO,
     *         or with status 400 (Bad Request) if the peliculaDTO is not valid,
     *         or with status 500 (Internal Server Error) if the peliculaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @Timed
    public ResponseEntity<PeliculaDTO> updatePelicula(@Valid @RequestBody PeliculaDTO peliculaDTO) throws URISyntaxException {
        log.debug("REST request to update Pelicula : {}", peliculaDTO);
        if (peliculaDTO.getId() == null) {
            return createPelicula(peliculaDTO);
        }
        Pelicula pelicula = peliculaMapper.toEntity(peliculaDTO);
        PeliculaDTO result = peliculaMapper.toDto(peliculaService.save(pelicula));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, peliculaDTO.getId().toString())).body(result);
    }

    /**
     * GET : get all the peliculas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of peliculas in body
     */
    @GetMapping
    @Timed
    public ResponseEntity<List<PeliculaDTO>> getAllPeliculas(@ApiParam(required = false) String query, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Peliculas");
        Page<PeliculaDTO> result = peliculaService.findAll(query, pageable).map(peliculaMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, BASE_URL);
        return new ResponseEntity<>(result.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /:id : get the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the peliculaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<PeliculaDTO> getPelicula(@PathVariable Long id) {
        log.debug("REST request to get Pelicula : {}", id);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(peliculaService.findOne(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(peliculaDTO));
    }

    /**
     * DELETE /:id : delete the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @Timed
    public ResponseEntity<Void> deletePelicula(@PathVariable Long id) {
        log.debug("REST request to delete Pelicula : {}", id);
        peliculaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE :batchDelete : delete the "id" pelicula.
     *
     * @param id the id of the peliculaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping
    @Timed
    public ResponseEntity<Void> batchDeletePeliculas(@ApiParam(required = false) String query) {
        log.debug("REST request to delete selected Peliculas");
        List<Pelicula> peliculasToDelete = getPeliculasToDelete(query);
        peliculasToDelete.stream().forEach(pelicula -> peliculaService.delete(pelicula.getId()));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, query)).build();
    }

    private List<Pelicula> getPeliculasToDelete(String query) {
        return peliculaService.findAll(query, null).getContent().stream().collect(Collectors.toList());
    }
}
