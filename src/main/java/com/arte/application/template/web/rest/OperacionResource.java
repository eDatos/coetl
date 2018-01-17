package com.arte.application.template.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arte.application.template.service.OperacionService;
import com.arte.application.template.web.rest.dto.OperacionDTO;
import com.arte.application.template.web.rest.mapper.OperacionMapper;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class OperacionResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(OperacionResource.class);

    private final OperacionService operacionService;

    private OperacionMapper operacionMapper;

    public OperacionResource(OperacionService operacionService, OperacionMapper operacionMapper) {
        this.operacionService = operacionService;
        this.operacionMapper = operacionMapper;
    }

    @GetMapping("/operaciones")
    @Timed
    @PreAuthorize("hasPermission('OPERACION', 'LEER')")
    public ResponseEntity<List<OperacionDTO>> getAllOperacions(@ApiParam(defaultValue = "") String query) {
        log.debug("REST petición para obtener una página de Operacions");
        List<OperacionDTO> operacionesDto = operacionMapper.toDto(operacionService.findAll(query));
        return new ResponseEntity<>(operacionesDto, null, HttpStatus.OK);
    }

    @GetMapping("/operaciones/{id}")
    @Timed
    @PreAuthorize("hasPermission('OPERACION', 'LEER')")
    public ResponseEntity<OperacionDTO> getOperacion(@PathVariable Long id) {
        log.debug("REST petición para obtener  Operacion : {}", id);
        OperacionDTO operacionDTO = operacionMapper.toDto(operacionService.findOne(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(operacionDTO));
    }

}
