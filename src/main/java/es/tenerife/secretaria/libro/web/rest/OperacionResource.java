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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.web.rest.dto.OperacionDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.OperacionMapper;
import es.tenerife.secretaria.libro.web.rest.util.PaginationUtil;
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
	public ResponseEntity<List<OperacionDTO>> getAllOperacions(@ApiParam Pageable pageable,
			@ApiParam(defaultValue = "") String query) {
		log.debug("REST petición para obtener una página de Operacions");
		Page<OperacionDTO> page = operacionService.findAll(pageable, query).map(operacionMapper::toDto);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operaciones");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
