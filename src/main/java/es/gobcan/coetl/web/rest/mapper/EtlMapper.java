package es.gobcan.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.repository.EtlRepository;
import es.gobcan.coetl.web.rest.dto.EtlDTO;

@Mapper(componentModel = "spring")
public abstract class EtlMapper implements EntityMapper<EtlDTO, Etl> {

    @Autowired
    EtlRepository etlRepository;

    private Etl fromId(Long id) {
        return etlRepository.findOne(id);
    }

    public Etl toEntity(EtlDTO dto) {
        if (dto == null) {
            return null;
        }

        Etl entity = (dto.getId() != null) ? fromId(dto.getId()) : new Etl();

        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setPurpose(dto.getPurpose());
        entity.setOrganizationInCharge(dto.getOrganizationInCharge());
        entity.setFunctionalInCharge(dto.getFunctionalInCharge());
        entity.setTechnicalInCharge(dto.getTechnicalInCharge());
        entity.setType(dto.getType());
        entity.setComments(dto.getComments());
        entity.setExecutionDescription(dto.getExecutionDescription());
        entity.setExecutionPlanning(dto.getExecutionPlanning());

        entity.setOptLock(dto.getOptLock());

        return entity;
    }
}
