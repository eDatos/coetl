package com.arte.application.template.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.domain.Operacion;
import com.arte.application.template.repository.OperacionRepository;
import com.arte.application.template.service.OperacionService;
import com.arte.application.template.web.rest.util.QueryUtil;

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
        DetachedCriteria criteria = queryUtil.queryToOperacionCriteria(null, query);
        return operacionRepository.findAll(criteria);
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
