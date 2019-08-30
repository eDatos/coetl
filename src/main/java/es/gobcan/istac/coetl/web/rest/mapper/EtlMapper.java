package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.repository.EtlRepository;
import es.gobcan.istac.coetl.web.rest.dto.EtlBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.EtlDTO;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public abstract class EtlMapper implements EntityMapper<EtlDTO, Etl> {

    @Autowired
    private EtlRepository etlRepository;

    @Autowired
    private FileMapper fileMapper;

    public Etl fromId(Long id) {
        return etlRepository.findOne(id);
    }

    @Override
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
        entity.setNextExecution(dto.getNextExecution());

        entity.setEtlFile(fileMapper.toEntity(dto.getEtlFile()));
        entity.setEtlDescriptionFile(fileMapper.toEntity(dto.getEtlDescriptionFile()));
        entity.addAllAttachedFiles(fileMapper.toEntity(dto.getAttachedFiles()));

        entity.setOptLock(dto.getOptLock());

        return entity;
    }

    public EtlBaseDTO toBaseDto(Etl entity) {
        if (entity == null) {
            return null;
        }

        EtlBaseDTO baseDto = new EtlBaseDTO();

        baseDto.setId(entity.getId());
        baseDto.setCode(entity.getCode());
        baseDto.setName(entity.getName());
        baseDto.setOrganizationInCharge(entity.getOrganizationInCharge());
        baseDto.setType(entity.getType());
        baseDto.setExecutionPlanning(entity.getExecutionPlanning());

        baseDto.setCreatedBy(entity.getCreatedBy());
        baseDto.setCreatedDate(entity.getCreatedDate());
        baseDto.setLastModifiedBy(entity.getLastModifiedBy());
        baseDto.setLastModifiedDate(entity.getLastModifiedDate());
        baseDto.setDeletedBy(entity.getDeletedBy());
        baseDto.setDeletionDate(entity.getDeletionDate());

        baseDto.setOptLock(entity.getOptLock());

        return baseDto;
    }
}
