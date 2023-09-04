package es.gobcan.istac.coetl.service.impl;

import es.gobcan.istac.coetl.domain.GlobalParameter;
import es.gobcan.istac.coetl.domain.Parameter.Typology;
import es.gobcan.istac.coetl.repository.GlobalParameterRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.service.GlobalParameterService;
import es.gobcan.istac.coetl.service.ParameterService;
import es.gobcan.istac.coetl.service.validator.GlobalParameterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GlobalParameterServiceImpl implements GlobalParameterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterService.class);

    @Autowired
    private GlobalParameterRepository gloablParameterRepository;

    @Autowired
    private GlobalParameterValidator globalParameterValidator;


    @Override
    public GlobalParameter create(GlobalParameter parameter) {
        LOGGER.debug("Request to create a Global Parameter : {}", parameter);
        globalParameterValidator.validate(parameter);
        return save(parameter);
    }

    @Override
    public GlobalParameter update(GlobalParameter parameter) {
        LOGGER.debug("Request to update a Global Parameter : {}", parameter);
        globalParameterValidator.validate(parameter);
        return save(parameter);
    }

    @Override
    public void delete(GlobalParameter parameter) {
        LOGGER.debug("Request to delete a Global Parameter : {}", parameter);
        gloablParameterRepository.delete(parameter);
    }

    @Override
    public Page<GlobalParameter> findAll(Pageable pageable) {
        LOGGER.debug("Request to find all Global Parameters");
        return gloablParameterRepository.findAll(pageable);
    }

    @Override
    public GlobalParameter findOneById(Long id) {
        LOGGER.debug("Request to get a Global Parameter : {}", id);
        return gloablParameterRepository.findOneById(id);
    }

    @Override
    public String decodeValueByTypology(GlobalParameter parameter){
        if(Typology.PASSWORD.equals(parameter.getTypology())){
            return SecurityUtils.passwordDecode(parameter.getValue());
        }
        return parameter.getValue();
    }

    @Override
    public Map<String, String> findAllGlobalParametersAsMap() {
        List<GlobalParameter> parameters = gloablParameterRepository.findAll();
        return parameters.stream().collect(Collectors.toMap(GlobalParameter::getKey, p -> decodeValueByTypology(p)));
    }

    private GlobalParameter save(GlobalParameter parameter) {
        LOGGER.debug("Request to save a Global Parameter : {}", parameter);
        return gloablParameterRepository.saveAndFlush(parameter);
    }
}
