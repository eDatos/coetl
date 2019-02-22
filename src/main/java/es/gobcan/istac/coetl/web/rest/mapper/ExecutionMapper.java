package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;

import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.web.rest.dto.ExecutionDTO;

@Mapper(componentModel = "spring")
public abstract class ExecutionMapper {

    public ExecutionDTO toDto(Execution entity) {
        if (entity == null) {
            return null;
        }

        ExecutionDTO dto = new ExecutionDTO();
        dto.setId(entity.getId());
        dto.setPlanningDate(entity.getPlanningDate());
        dto.setStartDate(entity.getStartDate());
        dto.setFinishDate(entity.getFinishDate());
        dto.setType(entity.getType());
        dto.setResult(entity.getResult());
        dto.setNotes(entity.getNotes());
        dto.setIdEtl(entity.getEtl().getId());

        return dto;
    }
}
