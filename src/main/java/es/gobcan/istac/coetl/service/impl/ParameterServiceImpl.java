package es.gobcan.istac.coetl.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.Parameter.Type;
import es.gobcan.istac.coetl.domain.Parameter.Typology;
import es.gobcan.istac.coetl.repository.ParameterRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
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
    public List<Parameter> createDefaultParameters(Etl etl, Map<String, String> parameters) {
        LOGGER.debug("Request to create default Parameters : {} of ETL : {}", parameters, etl);
        //@formatter:off
        return parameters.entrySet().stream()
                .map(entry -> {
                    Parameter parameter = new Parameter();
                    parameter.setEtl(etl);
                    parameter.setKey(entry.getKey());
                    parameter.setValue(entry.getValue());
                    parameter.setType(Type.AUTO);
                    parameter.setTypology(Typology.GENERIC);
                    return parameter;
                })
                .map(this::save)
                .collect(Collectors.toList());
        //@formatter:on
    }

    @Override
    public void deleteDefaultParameters(Etl etl) {
        LOGGER.debug("Request to delete default Parameters of ETL : {}", etl);
        List<Parameter> defaultParameters = parameterRepository.findAllByEtlIdAndType(etl.getId(), Type.AUTO);
        defaultParameters.forEach(this::delete);
        parameterRepository.flush();
    }

    @Override
    public Parameter create(Parameter parameter) {
        LOGGER.debug("Request to create a Parameter : {}", parameter);
        parameterValidator.validate(parameter);
        return save(parameter);
    }

    @Override
    public Parameter update(Parameter parameter) {
        LOGGER.debug("Request to update a Parameter : {}", parameter);
        parameterValidator.validate(parameter);
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
    public Map<String, String> findAllByEtlIdAsMap(Long etlId) {
        List<Parameter> parameters = findAllByEtlId(etlId);
        return parameters.stream().collect(Collectors.toMap(Parameter::getKey, p -> decodeValueByTypology(p)));
    }

    @Override
    public String decodeValueByTypology(Parameter parameter){
        if(Typology.PASSWORD.equals(parameter.getTypology())){
            return SecurityUtils.passwordDecode(parameter.getValue());
        }
        return parameter.getValue();
    }

    @Override
    public Parameter findOneByIdAndEtlId(Long id, Long etlId) {
        LOGGER.debug("Request to get a Parameter : {}", id);
        return parameterRepository.findByIdAndEtlId(id, etlId);
    }

    private Parameter save(Parameter parameter) {
        LOGGER.debug("Request to save a Parameter : {}", parameter);
        return parameterRepository.saveAndFlush(parameter);
    }
}
