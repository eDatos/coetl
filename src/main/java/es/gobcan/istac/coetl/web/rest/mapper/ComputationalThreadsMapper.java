package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsDTO;

@Mapper(componentModel = "spring", uses = {ExternalItemMapper.class})
public abstract class ComputationalThreadsMapper implements EntityMapper<ComputationalThreadsDTO, ComputationalThreads> {

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Autowired
    private ExternalItemMapper externalItemMapper;

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
        entity.setDescription(dto.getDescription());
        entity.setExecutionDescription(dto.getExecutionDescription());
        entity.setExecutionPlanning(dto.getExecutionPlanning());
        entity.setNextExecution(dto.getNextExecution());
        entity.setOptLock(dto.getOptLock());

        entity.setExternalItem(externalItemMapper.toEntity(dto.getExternalItem()));

        return entity;
    }

    /*public ComputationalThreadsDTO toDto(ComputationalThreads entity) {
        if (entity == null) {
            return null;
        }

        ComputationalThreadsDTO dto = new ComputationalThreadsDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
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

        dto.setExternalItem(externalItemMapper.toDto(entity.getExternalItem()));

        return dto;
    }*/

    public ComputationalThreadsBaseDTO toBaseDto(ComputationalThreads entity, Execution execution) {
        if (entity == null) {
            return null;
        }

        ComputationalThreadsBaseDTO baseDto = new ComputationalThreadsBaseDTO();

        baseDto.setId(entity.getId());
        baseDto.setCode(entity.getCode());
        baseDto.setName(entity.getName());
        baseDto.setExecutionDescription(entity.getExecutionDescription());
        baseDto.setExecutionPlanning(entity.getExecutionPlanning());
        baseDto.setNextExecution(entity.getNextExecution());
        baseDto.setCreatedBy(entity.getCreatedBy());
        baseDto.setCreatedDate(entity.getCreatedDate());
        baseDto.setLastModifiedBy(entity.getLastModifiedBy());
        baseDto.setLastModifiedDate(entity.getLastModifiedDate());
        baseDto.setDeletedBy(entity.getDeletedBy());
        baseDto.setDeletionDate(entity.getDeletionDate());

        baseDto.setOptLock(entity.getOptLock());

        baseDto.setExternalItem(externalItemMapper.toDto(entity.getExternalItem()));

        return baseDto;
    }

    public ComputationalThreadsBaseDTO toBaseDto(ComputationalThreads entity, String lastExecutionStartDate, String lastExecutionResult) {
        return toBaseDto(entity, null);
    }

}
