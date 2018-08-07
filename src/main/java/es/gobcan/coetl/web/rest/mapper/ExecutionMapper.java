package es.gobcan.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.web.rest.dto.ExecutionDTO;

@Mapper(componentModel = "spring", uses = {EtlMapper.class})
public abstract class ExecutionMapper implements EntityMapper<ExecutionDTO, Execution> {

    @Mapping(source = "idEtl", target = "etl")
    public abstract Execution toEntity(ExecutionDTO dto);

    public ExecutionDTO toDto(Execution entity) {
        if (entity == null) {
            return null;
        }

        ExecutionDTO dto = new ExecutionDTO();
        dto.setId(entity.getId());
        dto.setDatetime(entity.getDatetime());
        dto.setType(entity.getType());
        dto.setResult(entity.getResult());
        dto.setNotes(entity.getNotes());
        dto.setIdEtl(entity.getEtl().getId());

        return dto;
    }
}
