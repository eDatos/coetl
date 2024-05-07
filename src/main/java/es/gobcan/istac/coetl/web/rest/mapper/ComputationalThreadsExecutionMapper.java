package es.gobcan.istac.coetl.web.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecutionEtl;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.ComputationalThreadsEtl;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionEtlRepository;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionRepository;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadExecutionDTO;

@Mapper(componentModel = "spring")
public abstract class ComputationalThreadsExecutionMapper {

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Autowired
    ComputationalThreadExecutionRepository computationalThreadExecutionRepository;

    @Autowired
    ComputationalThreadExecutionEtlRepository computationalThreadExecutionEtlRepository;

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

    public ComputationalThreadExecution fromId(Long id) {
        return computationalThreadExecutionRepository.findOne(id);
    }

    public ComputationalThreadExecutionEtl fromExecutionId(Long id) {
        return computationalThreadExecutionEtlRepository.findOne(id);
    }

    public List<ComputationalThreadExecutionEtl> initializeThreadExecutionEtl(List<ComputationalThreadsEtl> computationalThreadsEtls) {
        return computationalThreadsEtls.stream().filter(Objects::nonNull).map(thread -> setNewThreadExecutionEtl()).collect(Collectors.toList());
    }

    public ComputationalThreadExecutionEtl setNewThreadExecutionEtl() {
        return new ComputationalThreadExecutionEtl();
    }

    public ComputationalThreadExecution toEntity(ComputationalThreadExecutionDTO computationalThreadExecutionDTO) {
        if (computationalThreadExecutionDTO == null) {
            return null;
        }

        ComputationalThreadExecution entity = (computationalThreadExecutionDTO.getId() != null) ? fromId(computationalThreadExecutionDTO.getId())
                : new ComputationalThreadExecution();
        ComputationalThreads thread = computationalThreadsRepository.findOne(computationalThreadExecutionDTO.getIdThread());
        entity.setComputationalThread(thread);
        entity.setComputationalThreadExecutionEtl(
                entity.getComputationalThreadExecutionEtl() == null ? new ArrayList<ComputationalThreadExecutionEtl>() : entity.getComputationalThreadExecutionEtl());
        entity.setType(computationalThreadExecutionDTO.getType());
        entity.setExecutor(computationalThreadExecutionDTO.getExecutor());
        entity.setResult(computationalThreadExecutionDTO.getResult());
        entity.setPlanningDate(computationalThreadExecutionDTO.getPlanningDate());
        entity.setNotes(computationalThreadExecutionDTO.getNotes());
        entity.setStartDate(computationalThreadExecutionDTO.getStartDate());
        entity.setFinishDate(computationalThreadExecutionDTO.getFinishDate());
        return entity;
    }

}
