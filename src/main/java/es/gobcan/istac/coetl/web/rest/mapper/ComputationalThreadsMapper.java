package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsDTO;

@Mapper(componentModel = "spring")
public abstract class ComputationalThreadsMapper implements EntityMapper<ComputationalThreadsDTO, ComputationalThreads> {

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    public ComputationalThreads fromId(Long id) {
        return computationalThreadsRepository.findOne(id);
    }

    @Override
    public ComputationalThreads toEntity(ComputationalThreadsDTO dto) {
        if (dto == null) {
            return null;
        }

        ComputationalThreads entity = (dto.getId() != null) ? fromId(dto.getId()) : new ComputationalThreads();

        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setPurpose(dto.getPurpose());
        entity.setOrganizationInCharge(dto.getOrganizationInCharge());
        entity.setFunctionalInCharge(dto.getFunctionalInCharge());
        entity.setTechnicalInCharge(dto.getTechnicalInCharge());
        entity.setComments(dto.getComments());
        entity.setExecutionDescription(dto.getExecutionDescription());
        entity.setExecutionPlanning(dto.getExecutionPlanning());
        entity.setNextExecution(dto.getNextExecution());
        entity.setOptLock(dto.getOptLock());

        return entity;
    }

    public ComputationalThreadsDTO toDto(ComputationalThreads entity) {
        if (entity == null) {
            return null;
        }

        ComputationalThreadsDTO dto = new ComputationalThreadsDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setPurpose(entity.getPurpose());
        dto.setOrganizationInCharge(entity.getOrganizationInCharge());
        dto.setFunctionalInCharge(entity.getFunctionalInCharge());
        dto.setTechnicalInCharge(entity.getTechnicalInCharge());
        dto.setComments(entity.getComments());
        dto.setExecutionDescription(entity.getExecutionDescription());
        dto.setExecutionPlanning(entity.getExecutionPlanning());
        dto.setNextExecution(entity.getNextExecution());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedBy(entity.getLastModifiedBy());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setDeletionDate(entity.getDeletionDate());

        dto.setOptLock(entity.getOptLock());

        return dto;
    }

}
