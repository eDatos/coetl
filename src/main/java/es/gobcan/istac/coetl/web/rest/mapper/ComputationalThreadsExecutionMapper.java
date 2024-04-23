package es.gobcan.istac.coetl.web.rest.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionRepository;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadExecutionDTO;

@Mapper(componentModel = "spring")
public abstract class ComputationalThreadsExecutionMapper {

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;
    
    @Autowired
    ComputationalThreadExecutionRepository computationalThreadExecutionRepository;

    public ComputationalThreadExecutionDTO toDto(ComputationalThreadExecution entity) {
        if (entity == null) {
            return null;
        }

        ComputationalThreadExecutionDTO dto = new ComputationalThreadExecutionDTO();
        dto.setId(entity.getId());
        dto.setPlanningDate(entity.getPlanningDate());
        dto.setStartDate(entity.getStartDate());
        dto.setFinishDate(entity.getFinishDate());
        dto.setType(entity.getType());
        dto.setResult(entity.getResult());
        dto.setNotes(entity.getNotes());
        dto.setIdThread(entity.getComputationalThread().getId());
        dto.setExecutor(entity.getExecutor());
        return dto;
    }

    private Result getResult(Long idThread, Result result) {
        if (!computationalThreadExecutionRepository.findAllByComputationalThreadIdAndResult(idThread, result).isEmpty()) {
            return Result.DUPLICATED;
        }
        return result;
    }

    public ComputationalThreadExecution toEntity(ComputationalThreadExecutionDTO computationalThreadExecutionDTO) {
        ComputationalThreadExecution computationalThreadExecution = new ComputationalThreadExecution();
        computationalThreadExecution.setComputationalThread(computationalThreadsRepository.findOne(computationalThreadExecutionDTO.getIdThread()));
        computationalThreadExecution.setType(computationalThreadExecutionDTO.getType());
        computationalThreadExecution.setExecutor(SecurityContextHolder.getContext().getAuthentication().getName());
        computationalThreadExecution.setResult(getResult(computationalThreadExecutionDTO.getIdThread(), computationalThreadExecutionDTO.getResult()));
        computationalThreadExecution.setPlanningDate(Instant.now());
        computationalThreadExecution.setNotes(computationalThreadExecutionDTO.getNotes());
        if (Result.RUNNING.equals(computationalThreadExecutionDTO.getResult())) {
            computationalThreadExecution.setStartDate(Instant.now());
        }
        return computationalThreadExecution;
    }

}
