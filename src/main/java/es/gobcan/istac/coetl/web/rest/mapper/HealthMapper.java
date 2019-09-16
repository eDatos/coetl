package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.Health;
import es.gobcan.istac.coetl.repository.HealthRepository;
import es.gobcan.istac.coetl.web.rest.dto.HealthDTO;

@Mapper(componentModel = "spring", uses = {})
public abstract class HealthMapper implements EntityMapper<HealthDTO, Health> {

    @Autowired
    private HealthRepository healthRepository;

    public Health fromId(Long id) {
        return healthRepository.findOne(id);
    }

    @Override
    public Health toEntity(HealthDTO dto) {
        if (dto == null) {
            return null;
        }

        Health entity = (dto.getId() != null) ? fromId(dto.getId()) : new Health();

        entity.setServiceName(dto.getServiceName());
        entity.setEndpoint(dto.getEndpoint());

        return entity;
    }
}