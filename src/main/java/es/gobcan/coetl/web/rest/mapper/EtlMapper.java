package es.gobcan.coetl.web.rest.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.repository.EtlRepository;
import es.gobcan.coetl.web.rest.dto.EtlDTO;

@Mapper(componentModel = "spring")
public abstract class EtlMapper implements EntityMapper<EtlDTO, Etl> {

    @Autowired
    EtlRepository etlRepository;

    public Etl fromId(Long id) {
        return (id == null) ? null : etlRepository.findOne(id);
    }

    public Etl toEntity(EtlDTO dto) {
        if (dto == null) {
            return null;
        }

        Etl entity = (dto.getId() != null) ? fromId(dto.getId()) : new Etl();

        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setPurpose((StringUtils.isNotBlank(dto.getPurpose())) ? dto.getPurpose() : null);
        entity.setOrganizationInCharge(dto.getOrganizationInCharge());
        entity.setFunctionalInCharge(dto.getFunctionalInCharge());
        entity.setTechnicalInCharge(dto.getTechnicalInCharge());
        entity.setType(dto.getType());
        entity.setComments((StringUtils.isNotBlank(dto.getComments())) ? dto.getComments() : null);
        entity.setExecutionDescription((StringUtils.isNotBlank(dto.getExecutionDescription())) ? dto.getExecutionDescription() : null);
        entity.setExecutionPlanning((StringUtils.isNotBlank(dto.getExecutionPlanning())) ? dto.getExecutionPlanning() : null);
        entity.setNextExecution(dto.getNextExecution());

        entity.setOptLock(dto.getOptLock());

        return entity;
    }
}
