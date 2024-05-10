package es.gobcan.istac.coetl.web.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.ComputationalThreadsEtl;
import es.gobcan.istac.coetl.repository.ComputationalThreadExecutionRepository;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.service.EtlService;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsDTO;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsEtlDTO;

@Mapper(componentModel = "spring", uses = { ExternalItemMapper.class, ComputationalThreadsEtlMapper.class })
public abstract class ComputationalThreadsMapper implements EntityMapper<ComputationalThreadsDTO, ComputationalThreads> {

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Autowired
    private ComputationalThreadsEtlMapper computationalThreadsEtlMapper;

    @Autowired
    private ExternalItemMapper externalItemMapper;

    @Autowired
    private EtlService etlService;

    @Autowired
    private ComputationalThreadExecutionRepository computationalThreadExecutionRepository;

    private ComputationalThreads fromId(Long id) {
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

        entity.setExternalItem(externalItemMapper.toEntity(dto.getExternalItem()));

        entity.setOptLock(dto.getOptLock());

        if (dto.getComputationalThreadsEtl().isEmpty()) {
            entity.setComputationalThreadsEtl(new ArrayList<ComputationalThreadsEtl>());
        } else {
            entity.setComputationalThreadsEtl(computationalThreadEtlDTOsToEntity(dto.getComputationalThreadsEtl(), dto.getId()));
        }

        return entity;
    }

    public List<ComputationalThreadsEtl> computationalThreadEtlDTOsToEntity(List<ComputationalThreadsEtlDTO> computationalThreadsEtlDTOs, Long idThread) {
        return computationalThreadsEtlDTOs.stream().filter(Objects::nonNull)
                .map(computationalThreadsEtlDTO -> computationalThreadEtlDTOToEntity(computationalThreadsEtlDTO, idThread)).collect(Collectors.toList());
    }

    public ComputationalThreadsEtl computationalThreadEtlDTOToEntity(ComputationalThreadsEtlDTO computationalThreadsEtlDTO, Long idThread) {
        if (computationalThreadsEtlDTO == null) {
            return null;
        } else if (computationalThreadsEtlDTO.getId() == null) {
            ComputationalThreadsEtl computationalThreadEtltmp = new ComputationalThreadsEtl();
            computationalThreadEtltmp.setComputationalThread(computationalThreadsRepository.findOne(idThread));
            computationalThreadEtltmp.setEtl(etlService.findOne(computationalThreadsEtlDTO.getEtl().getId()));
            computationalThreadEtltmp.setExecutionOrder(computationalThreadsEtlDTO.getExecutionOrder());
            return computationalThreadEtltmp;
        } else {
            ComputationalThreadsEtl computationalThreadEtltmp = computationalThreadsEtlMapper.toEntity(computationalThreadsEtlDTO);
            computationalThreadEtltmp.setComputationalThread(computationalThreadsRepository.findOne(idThread));
            return computationalThreadEtltmp;
        }
    }

    private void setDataExecution(ComputationalThreadExecution execution, ComputationalThreadsBaseDTO baseDto) {
        if (execution != null) {
            baseDto.setLastExecution(execution.getStartDate());
            baseDto.setResult(execution.getResult());
        }
    }

    public ComputationalThreadsBaseDTO toBaseDto(ComputationalThreads entity, ComputationalThreadExecution execution) {
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

        setDataExecution(execution, baseDto);

        baseDto.setOptLock(entity.getOptLock());

        baseDto.setExternalItem(externalItemMapper.toDto(entity.getExternalItem()));

        baseDto.setComputationalThreadsEtl(entity.getComputationalThreadsEtl().stream().map(e -> computationalThreadsEtlMapper.toDto(e)).collect(Collectors.toList()));

        return baseDto;
    }

    public ComputationalThreadsBaseDTO toBaseDto(ComputationalThreads entity, String lastExecutionStartDate, String lastExecutionResult) {
        ComputationalThreadExecution execution;
        if (StringUtils.isNotBlank(lastExecutionResult) && StringUtils.isNotBlank(lastExecutionStartDate)) {
            execution = computationalThreadExecutionRepository.findFirstByComputationalThreadIdAndPlanningDateAndResultOrderByIdDesc(entity.getId(), lastExecutionStartDate, lastExecutionResult);
        } else if (StringUtils.isNotBlank(lastExecutionResult) && StringUtils.isBlank(lastExecutionStartDate)) {
            execution = computationalThreadExecutionRepository.findFirstByComputationalThreadIdAndResultOrderByIdDesc(entity.getId(), Result.valueOf(lastExecutionResult));
        } else if (StringUtils.isBlank(lastExecutionResult) && StringUtils.isNotBlank(lastExecutionStartDate)) {
            execution = computationalThreadExecutionRepository.findFirstByComputationalThreadIdAndPlanningDateOrderByIdDesc(entity.getId(), lastExecutionStartDate);
        } else {
            execution = computationalThreadExecutionRepository.findFirstByComputationalThreadIdOrderByPlanningDateDesc(entity.getId());
        }
        return toBaseDto(entity, execution);
    }

}
