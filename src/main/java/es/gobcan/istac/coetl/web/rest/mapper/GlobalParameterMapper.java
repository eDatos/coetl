package es.gobcan.istac.coetl.web.rest.mapper;

import es.gobcan.istac.coetl.domain.GlobalParameter;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.repository.GlobalParameterRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.web.rest.dto.GlobalParameterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GlobalParameterMapper {

    @Autowired
    private GlobalParameterRepository globalParameterRepository;

    public GlobalParameter fromId(Long id) {
        return id != null ? globalParameterRepository.findOne(id) : null;
    }

    public GlobalParameter toEntity(GlobalParameterDTO dto) {
        if (dto == null) {
            return null;
        }

        GlobalParameter entity = dto.getId() != null ? fromId(dto.getId()) : new GlobalParameter();

        entity.setKey(dto.getKey());
        entity.setValue(setEncodeValueByTypology(dto.getTypology(),dto.getValue()));
        entity.setTypology(dto.getTypology());
        entity.setOptLock(dto.getOptLock());

        return entity;
    }

    private String setEncodeValueByTypology(GlobalParameter.Typology typology, String value){
        if(Parameter.Typology.PASSWORD.equals(typology)){
            String encodeValue = SecurityUtils.passwordEncoder(value);
            return encodeValue;
        }
        return value;
    }

    public GlobalParameterDTO toDto(GlobalParameter entity) {
        if (entity == null) {
            return null;
        }

        GlobalParameterDTO dto = new GlobalParameterDTO();
        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setValue(entity.getValue());
        dto.setTypology(entity.getTypology());

        dto.setOptLock(entity.getOptLock());

        return dto;
    }

    public List<GlobalParameterDTO> toDto(List<GlobalParameter> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}
