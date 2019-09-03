package es.gobcan.istac.coetl.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.repository.ParameterRepository;
import es.gobcan.istac.coetl.service.ParameterService;
import es.gobcan.istac.coetl.service.validator.ParameterValidator;

@Service
public class ParameterServiceImpl implements ParameterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterService.class);

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterValidator parameterValidator;

    @Override
    public Parameter create(Parameter parameter) {
        LOGGER.debug("Request to create a Parameter : {}", parameter);
        return save(parameter);
    }

    @Override
    public Parameter update(Parameter parameter) {
        LOGGER.debug("Request to update a Parameter : {}", parameter);
        return save(parameter);
    }

    @Override
    public void delete(Parameter parameter) {
        LOGGER.debug("Request to delete a Parameter : {}", parameter);
        parameterRepository.delete(parameter);
    }

    @Override
    public List<Parameter> findAllByEtlId(Long etlId) {
        LOGGER.debug("Request to find all Parameters by ETL: {}", etlId);
        return parameterRepository.findAllByEtlId(etlId);
    }

    @Override
    public Parameter findOneByIdAndEtlId(Long id, Long etlId) {
        LOGGER.debug("Request to get a Parameter : {}", id);
        return parameterRepository.findByIdAndEtlId(id, etlId);
    }

    private Parameter save(Parameter parameter) {
        LOGGER.debug("Request to save a Parameter : {}", parameter);
        parameterValidator.validate(parameter);
        return parameterRepository.saveAndFlush(parameter);
    }
}
