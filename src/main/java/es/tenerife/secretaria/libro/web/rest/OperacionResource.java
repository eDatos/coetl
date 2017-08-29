package es.tenerife.secretaria.libro.web.rest;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.web.rest.dto.OperacionDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.OperacionMapper;
import es.tenerife.secretaria.libro.web.rest.util.HeaderUtil;
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
	 * POST /operaciones : Create a new operacion.
	 *
	 * @param operacionDTO
	 *            the operacionDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         operacionDTO, or with status 400 (Bad Request) if the operacion has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/operaciones")
	@Timed
	public ResponseEntity<OperacionDTO> createOperacion(@Valid @RequestBody OperacionDTO operacionDTO)
			throws URISyntaxException {
		log.debug("REST request to save Operacion : {}", operacionDTO);
		if (operacionDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new operacion cannot already have an ID"))
					.body(null);
		}
		OperacionDTO result = operacionMapper.toDto(operacionService.save(operacionMapper.toEntity(operacionDTO)));
		return ResponseEntity.created(new URI("/api/operaciones/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /operaciones : Updates an existing operacion.
	 *
	 * @param operacionDTO
	 *            the operacionDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         operacionDTO, or with status 400 (Bad Request) if the operacionDTO is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         operacionDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/operaciones")
	@Timed
	public ResponseEntity<OperacionDTO> updateOperacion(@Valid @RequestBody OperacionDTO operacionDTO)
			throws URISyntaxException {
		log.debug("REST request to update Operacion : {}", operacionDTO);
		if (operacionDTO.getId() == null) {
			return createOperacion(operacionDTO);
		}
		Operacion operacion = operacionService.findOne(operacionDTO.getId());
		OperacionDTO result = operacionMapper
				.toDto(operacionService.save(operacionMapper.update(operacion, operacionDTO)));
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operacionDTO.getId().toString())).body(result);
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
	public ResponseEntity<List<OperacionDTO>> getAllOperacions(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Operacions");
		Page<OperacionDTO> page = operacionService.findAll(pageable).map(operacionMapper::toDto);
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

	/**
	 * DELETE /operaciones/:id : delete the "id" operacion.
	 *
	 * @param id
	 *            the id of the operacionDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/operaciones/{id}")
	@Timed
	public ResponseEntity<Void> deleteOperacion(@PathVariable Long id) {
		log.debug("REST request to delete Operacion : {}", id);
		operacionService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
