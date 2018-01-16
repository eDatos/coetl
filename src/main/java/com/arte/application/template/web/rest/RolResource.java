package com.arte.application.template.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.arte.application.template.config.AuditConstants;
import com.arte.application.template.config.audit.AuditEventPublisher;
import com.arte.application.template.domain.Rol;
import com.arte.application.template.service.RolService;
import com.arte.application.template.web.rest.dto.RolDTO;
import com.arte.application.template.web.rest.dto.Views;
import com.arte.application.template.web.rest.errors.ErrorConstants;
import com.arte.application.template.web.rest.mapper.RolMapper;
import com.arte.application.template.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class RolResource extends AbstractResource {

	private static final String ENTITY_NAME = "rol";

	private final Logger log = LoggerFactory.getLogger(RolResource.class);

	private RolService rolService;
	private RolMapper rolMapper;
	private AuditEventPublisher auditPublisher;

	public RolResource(RolService rolService, RolMapper rolMapper, AuditEventPublisher auditPublisher) {
		this.rolService = rolService;
		this.rolMapper = rolMapper;
		this.auditPublisher = auditPublisher;

	}

	@GetMapping("/roles")
	@PreAuthorize("hasPermission('ROL', 'LEER')")
	@Timed
	@JsonView(Views.Minimal.class)
	public ResponseEntity<List<RolDTO>> getRoles(@ApiParam(required = false) Long operacionId,
			@ApiParam(required = false) String query) {
		log.debug("REST petición para obtener una página de Roles");
		List<Rol> roles;
		if (operacionId != null) {
			roles = rolService.findByOperacion(operacionId);
		} else {
			roles = rolService.findAll(query);
		}
		return new ResponseEntity<>(rolMapper.toDto(roles), null, HttpStatus.OK);
	}

	@GetMapping("/roles/{codigo}")
	@PreAuthorize("hasPermission('ROL', 'LEER')")
	@Timed
	public ResponseEntity<RolDTO> getRol(@PathVariable String codigo) {
		log.debug("REST petición para obtener Rol : {}", codigo);
		RolDTO rolDTO = rolMapper.toDto(rolService.findOne(codigo));
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rolDTO));
	}

	@PostMapping("/roles")
	@PreAuthorize("hasPermission('ROL', 'CREAR')")
	@Timed
	public ResponseEntity<RolDTO> createRol(@RequestBody RolDTO rolDTO) throws URISyntaxException {
		log.debug("REST request to create Rol {}", rolDTO);
		Rol rol;
		if (rolDTO.getCodigo() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
					ErrorConstants.CODIGO_FALTA, "Un nuevo rol necesita un codigo")).body(null);
		}
		rol = rolService.findOne(rolDTO.getCodigo());
		if (rol != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ENTIDAD_EXISTE, "El rol ya exsitía"))
					.body(null);
		}
		rol = rolService.save(rolMapper.toEntity(rolDTO));
		auditPublisher.publish(AuditConstants.ROL_CREACION, rolDTO.getCodigo());
		return ResponseEntity.created(new URI("/api/usuarios/" + rol.getCodigo()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, rol.getCodigo())).body(rolMapper.toDto(rol));
	}

	@PutMapping("/roles")
	@PreAuthorize("hasPermission('ROL', 'EDITAR')")
	@Timed
	public ResponseEntity<RolDTO> updateRol(@RequestBody RolDTO rolDTO) {
		log.debug("REST petición para actualizar Rol {}", rolDTO);
		if (rolDTO.getCodigo() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
					ErrorConstants.CODIGO_FALTA, "Un rol necesita un codigo")).body(null);
		}
		if (rolDTO.getId() == null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_FALTA, "Se necesita un identificador"))
					.body(null);
		}
		Rol rol = rolService.findOne(rolDTO.getId());
		if (rol == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "no-encontrado",
					"El rol a actualizar no se ha encontrado")).body(null);
		}
		rolMapper.update(rol, rolDTO);
		rolService.save(rol);
		auditPublisher.publish(AuditConstants.ROL_EDICION, rolDTO.getCodigo());
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rol.getCodigo()))
				.body(rolMapper.toDto(rol));
	}

	@DeleteMapping("/roles/{codigo}")
	@Timed
	@PreAuthorize("hasPermission('ROL', 'ELIMINAR')")
	public ResponseEntity<RolDTO> deleteRol(@PathVariable String codigo) {
		log.debug("REST petición para eliminar Rol : {}", codigo);
		rolService.delete(codigo);
		auditPublisher.publish(AuditConstants.ROL_BORRADO, codigo);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, codigo)).build();
	}
}
