package com.arte.application.template.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arte.application.template.domain.Documento;
import com.arte.application.template.service.DocumentoService;
import com.arte.application.template.web.rest.dto.DocumentoDTO;
import com.arte.application.template.web.rest.errors.ErrorConstants;
import com.arte.application.template.web.rest.mapper.DocumentoMapper;
import com.arte.application.template.web.rest.util.ControllerUtil;
import com.arte.application.template.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoResource.class);

    private static final String ENTITY_NAME = "documento";
    private static final String BASE_URL = "/api/documentos";

    private final DocumentoService documentoService;

    private DocumentoMapper documentoMapper;

    public DocumentoResource(DocumentoService documentoService, DocumentoMapper documentoMapper) {
        this.documentoService = documentoService;
        this.documentoMapper = documentoMapper;
    }

    @PostMapping
    @Timed
    public ResponseEntity<DocumentoDTO> createDocumento(@RequestParam("file") MultipartFile file) throws URISyntaxException {
        log.debug("REST petición para guardar Documento : {}", file);
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.FICHERO_VACIO, "Uploaded file is empty")).body(null);
        }
        Documento result = documentoService.create(file);
        DocumentoDTO resultDTO = documentoMapper.toDto(result);
        return ResponseEntity.created(new URI(BASE_URL + "/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(resultDTO);
    }

    @PutMapping
    @Timed
    public ResponseEntity<DocumentoDTO> updateDocumento(@Valid @RequestBody DocumentoDTO documentoDTO) {
        log.debug("REST petición para editar Documento : {}", documentoDTO);
        if (documentoDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_FALTA, "An id is required")).body(null);
        }
        Documento documento = documentoService.findOne(documentoDTO.getId());
        documento = documentoMapper.update(documento, documentoDTO);
        documento = documentoService.save(documento);
        DocumentoDTO result = documentoMapper.toDto(documento);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentoDTO.getId().toString())).body(result);
    }

    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<DocumentoDTO> getDocumento(@PathVariable Long id) {
        log.debug("REST petición para obtener Documento : {}", id);
        Documento documento = documentoService.findOne(id);
        if (documento == null) {
            return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ENTIDAD_NO_ENCONTRADA, "Entity requested was not found")).build();
        }
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentoDTO));
    }

    @GetMapping(value = "/{id}/download", consumes = "*/*", produces = "*/*")
    @Timed
    public void getDocumento(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST petición para obtener Documento : {}", id);
        Documento documento = documentoService.findOne(id);
        if (documento == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            ControllerUtil.download(documento, response);
        }
    }

    @DeleteMapping("/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumento(@PathVariable Long id) {
        log.debug("REST petición para eliminar Documento : {}", id);
        documentoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
