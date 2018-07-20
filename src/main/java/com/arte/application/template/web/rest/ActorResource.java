package com.arte.application.template.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import com.arte.application.template.domain.Actor;
import com.arte.application.template.domain.Documento;
import com.arte.application.template.service.ActorService;
import com.arte.application.template.service.DocumentoService;
import com.arte.application.template.web.rest.dto.ActorDTO;
import com.arte.application.template.web.rest.errors.ErrorConstants;
import com.arte.application.template.web.rest.mapper.ActorMapper;
import com.arte.application.template.web.rest.util.HeaderUtil;
import com.arte.application.template.web.rest.util.PaginationUtil;
import com.arte.application.template.web.rest.util.WebConstants;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Actor.
 */
@RestController
@RequestMapping("/api/actores")
public class ActorResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "actor";
    private static final String BASE_URL = "/api/actores";

    private final ActorService actorService;

    private final ActorMapper actorMapper;

    private final DocumentoService documentoService;

    public ActorResource(ActorService actorService, ActorMapper actorMapper, DocumentoService documentoService) {
        this.actorService = actorService;
        this.actorMapper = actorMapper;
        this.documentoService = documentoService;
    }

    /**
     * POST : Create a new actor.
     *
     * @param actorDTO the actorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actorDTO, or with status 400 (Bad Request) if the actor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.puedeCrearActor(authentication)")
    public ResponseEntity<ActorDTO> createActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actorDTO);
        if (actorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new actor cannot already have an ID")).body(null);
        }
        Actor actor = actorService.save(actorMapper.toEntity(actorDTO));
        ActorDTO result = actorMapper.toDto(actor);
        return ResponseEntity.created(new URI(BASE_URL + "/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT : Updates an existing actor.
     *
     * @param actorDTO the actorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actorDTO,
     *         or with status 400 (Bad Request) if the actorDTO is not valid,
     *         or with status 500 (Internal Server Error) if the actorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.puedeModificarActor(authentication)")
    public ResponseEntity<ActorDTO> updateActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to update Actor : {}", actorDTO);
        if (actorDTO.getId() == null) {
            return createActor(actorDTO);
        }
        Actor actor = actorService.save(actorMapper.toEntity(actorDTO));
        ActorDTO result = actorMapper.toDto(actor);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actorDTO.getId().toString())).body(result);
    }

    /**
     * GET : get all the actors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actors in body
     */
    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarActor(authentication)")
    public ResponseEntity<List<ActorDTO>> getAllActores(@ApiParam(required = false) String query, @ApiParam Pageable pageable) {
        log.debug("REST request to get all Actors");
        Page<ActorDTO> result = actorService.findAll(query, pageable).map(actorMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, BASE_URL);
        return new ResponseEntity<>(result.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /:id : get the "id" actor.
     *
     * @param id the id of the actorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarActor(authentication)")
    public ResponseEntity<ActorDTO> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        ActorDTO actorDTO = actorMapper.toDto(actorService.findOne(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(actorDTO));
    }

    /**
     * DELETE /actors/:id : delete the "id" actor.
     *
     * @param id the id of the actorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.puedeBorrarActor(authentication)")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * PUT /:actorId/documento/:documentoId : Bind a documento to actor
     * 
     * @param actorId the id of the actor
     * @param documentoId the id of the documento
     * @return the ResponseEntity with status 200 (OK) and with body the updated actor,
     *         or with status 400 (Bad Request) if the actorId or documentoId is not valid,
     *         or with status 500 (Internal Server Error) if the actor couldn't be updated
     */
    @PutMapping("/{actorId}/documento/{documentoId}")
    @Timed
    @PreAuthorize("@secChecker.puedeModificarActor(authentication)")
    public ResponseEntity<ActorDTO> bindDocumento(@Valid @PathVariable Long actorId, @PathVariable Long documentoId) {
        log.debug("REST request to bind a Documento to Actor : {}", actorId);
        if (actorId == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_FALTA, WebConstants.ID_MISSING_MESSAGE)).build();
        }

        Actor actor = actorService.findOne(actorId);
        if (actor == null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ENTIDAD_NO_ENCONTRADA, String.format(WebConstants.ENTITY_NOT_FOUND_MESSAGE, ENTITY_NAME))).build();
        }

        Documento documento = documentoService.findOne(documentoId);
        if (documento == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(WebConstants.ENTITY_NAME_DOCUMENTO, ErrorConstants.ENTIDAD_NO_ENCONTRADA,
                    String.format(WebConstants.ENTITY_NOT_FOUND_MESSAGE, WebConstants.ENTITY_NAME_DOCUMENTO))).build();
        }

        ActorDTO result = actorMapper.toDto(actorService.bindDocumento(actor, documento));

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * DELETE /:actorId/documento/:documentoId : Unbind a documento to actor
     * 
     * @param actorId the id of the actor
     * @param documentoId the id of the documento
     * @return the ResponseEntity with status 200 (OK) and with body the updated actor,
     *         or with status 400 (Bad Request) if the actorId or documentoId is not valid,
     *         or with status 500 (Internal Server Error) if the actor couldn't be updated
     */
    @DeleteMapping("/{actorId}/documento/{documentoId}")
    @Timed
    @PreAuthorize("@secChecker.puedeModificarActor(authentication)")
    public ResponseEntity<ActorDTO> unbindDocumento(@Valid @PathVariable Long actorId, @PathVariable Long documentoId) {
        log.debug("REST request to unbind a Documento to Actor : {}", actorId);
        if (actorId == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_FALTA, WebConstants.ID_MISSING_MESSAGE)).build();
        }

        Actor actor = actorService.findOne(actorId);
        if (actor == null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ENTIDAD_NO_ENCONTRADA, String.format(WebConstants.ENTITY_NOT_FOUND_MESSAGE, ENTITY_NAME))).build();
        }

        Documento documento = documentoService.findOne(documentoId);
        if (documento == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(WebConstants.ENTITY_NAME_DOCUMENTO, ErrorConstants.ENTIDAD_NO_ENCONTRADA,
                    String.format(WebConstants.ENTITY_NOT_FOUND_MESSAGE, WebConstants.ENTITY_NAME_DOCUMENTO))).build();
        }

        ActorDTO result = actorMapper.toDto(actorService.unbindDocumento(actor, documento));

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }
}
