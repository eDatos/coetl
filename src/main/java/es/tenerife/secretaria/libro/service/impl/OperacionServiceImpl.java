package es.tenerife.secretaria.libro.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.repository.OperacionRepository;
import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.web.rest.util.QueryUtil;

@Service
public class OperacionServiceImpl implements OperacionService {

	private final Logger log = LoggerFactory.getLogger(OperacionServiceImpl.class);

	private final OperacionRepository operacionRepository;

	private QueryUtil queryUtil;

	public OperacionServiceImpl(OperacionRepository operacionRepository, QueryUtil queryUtil) {
		this.operacionRepository = operacionRepository;
		this.queryUtil = queryUtil;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Operacion> findAll(String query) {
		log.debug("Petición para obtener todas las Operaciones");
		DetachedCriteria criteria = queryUtil.queryToOperacionCriteria(query);
		return operacionRepository.findAll(criteria);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Operacion> findAll() {
		log.debug("Petición para obtener todas las Operaciones sin paginación");
		return operacionRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Operacion findOne(Long id) {
		log.debug("Petición para obtener Operacion : {}", id);
		return operacionRepository.findOne(id);
	}

	@Override
	public Operacion findBySujetoAndAccion(String sujeto, String accion) {
		log.debug("Petición para obtener Operacion : {}, {}", sujeto, accion);
		return operacionRepository.findBySujetoAndAccion(sujeto, accion);

	}

}
