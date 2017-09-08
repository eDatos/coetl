package es.tenerife.secretaria.libro.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.repository.OperacionRepository;
import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.web.rest.util.QueryUtil;

/**
 * Service Implementation for managing Operacion.
 */
@Service
public class OperacionServiceImpl implements OperacionService {

	private final Logger log = LoggerFactory.getLogger(OperacionServiceImpl.class);

	private final OperacionRepository operacionRepository;

	private QueryUtil queryUtil;

	public OperacionServiceImpl(OperacionRepository operacionRepository, QueryUtil queryUtil) {
		this.operacionRepository = operacionRepository;
		this.queryUtil = queryUtil;
	}

	/**
	 * Save a operacion.
	 *
	 * @param operacion
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public Operacion save(Operacion operacion) {
		log.debug("Request to save Operacion : {}", operacion);
		return operacionRepository.save(operacion);
	}

	/**
	 * Get all the operacions.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Operacion> findAll(Pageable pageable, String query) {
		log.debug("Request to get all Operacions");
		DetachedCriteria criteria = queryUtil.queryToOperacionCriteria(query);
		return operacionRepository.findAll(criteria, pageable);
	}

	/**
	 * Get one operacion by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Operacion findOne(Long id) {
		log.debug("Request to get Operacion : {}", id);
		return operacionRepository.findOne(id);
	}

	/**
	 * Delete the operacion by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Operacion : {}", id);
		operacionRepository.delete(id);
	}
}
