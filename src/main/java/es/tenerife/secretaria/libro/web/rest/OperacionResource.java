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

/**
 * REST controller for managing Operacion.
 */
@RestController
@RequestMapping("/api")
public class OperacionResource extends AbstractResource {

	private final Logger log = LoggerFactory.getLogger(OperacionResource.class);

	private static final String ENTITY_NAME = "operacion";

	private final OperacionService operacionService;

	private OperacionMapper operacionMapper;

	public OperacionResource(OperacionService operacionService, OperacionMapper operacionMapper) {
		this.operacionService = operacionService;
		this.operacionMapper = operacionMapper;
	}

	/**
	 * GET /operaciones : get all the operaciones.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of operaciones
	 *         in body
	 */
	@GetMapping("/operaciones")
	@Timed
	public ResponseEntity<List<OperacionDTO>> getAllOperacions(@ApiParam Pageable pageable,
			@ApiParam(defaultValue = "") String query) {
		log.debug("REST request to get a page of Operacions");
		Page<OperacionDTO> page = operacionService.findAll(pageable, query).map(operacionMapper::toDto);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operaciones");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /operaciones/:id : get the "id" operacion.
	 *
	 * @param id
	 *            the id of the operacionDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         operacionDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/operaciones/{id}")
	@Timed
	public ResponseEntity<OperacionDTO> getOperacion(@PathVariable Long id) {
		log.debug("REST request to get Operacion : {}", id);
		OperacionDTO operacionDTO = operacionMapper.toDto(operacionService.findOne(id));
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(operacionDTO));
	}

}
