package es.tenerife.secretaria.libro.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.security.AuthoritiesConstants;
import es.tenerife.secretaria.libro.service.RolService;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.RolMapper;
import es.tenerife.secretaria.libro.web.rest.util.HeaderUtil;
import es.tenerife.secretaria.libro.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class RolResource extends AbstractResource {

	private static final String ENTITY_NAME = "rol";

	private final Logger log = LoggerFactory.getLogger(RolResource.class);

	private RolService rolService;
	private RolMapper rolMapper;

	public RolResource(RolService rolService, RolMapper rolMapper) {
		this.rolService = rolService;
		this.rolMapper = rolMapper;

	}

	@GetMapping("/roles")
	@Timed
	@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<List<RolDTO>> getRoles(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Roles");
		Page<RolDTO> page = rolService.findAll(pageable).map(rolMapper::toDto);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/roles/{nombre}")
	@Timed
	public ResponseEntity<RolDTO> getRol(@PathVariable String nombre) {
		log.debug("REST request to get Rol : {}", nombre);
		RolDTO rolDTO = rolMapper.toDto(rolService.findOne(nombre));
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rolDTO));
	}

	@PostMapping("/roles")
	@Timed
	public ResponseEntity<RolDTO> createRol(@RequestBody RolDTO rolDTO) {
		log.debug("REST request to create Rol {}", rolDTO);
		Rol rol;
		if (rolDTO.getNombre() == null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "nombre-falta", "Un nuevo rol necesita un nombre"))
					.body(null);
		}
		rol = rolService.findOne(rolDTO.getNombre());
		if (rol != null) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "rol-existe", "El rol ya exsitía")).body(null);
		}
		rol = rolService.save(rolMapper.toEntity(rolDTO));
		return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, rol.getNombre()))
				.body(rolMapper.toDto(rol));
	}

	@PutMapping("/roles")
	@Timed
	public ResponseEntity<RolDTO> updateRol(@RequestBody RolDTO rolDTO) {
		log.debug("REST request to update Rol {}", rolDTO);
		if (rolDTO.getNombre() == null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "nombre-falta", "Un nuevo rol necesita un nombre"))
					.body(null);
		}
		Rol rol = rolService.findOne(rolDTO.getNombre());
		if (rol == null) {
			return createRol(rolDTO);
		}
		rolMapper.update(rol, rolDTO);
		rolService.save(rol);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rol.getNombre()))
				.body(rolMapper.toDto(rol));
	}
}
