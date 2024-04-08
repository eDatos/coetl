package es.gobcan.istac.coetl.web.rest.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.ComputationalThreadsEtl;
import es.gobcan.istac.coetl.domain.Execution;
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

        entity.setExternalItem(externalItemMapper.toEntity(dto.getExternalItem()));

        entity.setOptLock(dto.getOptLock());

        entity.setComputationalThreadsEtl(computationalThreadEtlDTOsToEntity(dto.getComputationalThreadsEtl()));

        return entity;
    }

    public List<ComputationalThreadsEtl> computationalThreadEtlDTOsToEntity(List<ComputationalThreadsEtlDTO> computationalThreadsDTOs) {
        return computationalThreadsDTOs.stream().filter(Objects::nonNull).map(this::computationalThreadEtlDTOToEntity).collect(Collectors.toList());
    }

    public ComputationalThreadsEtl computationalThreadEtlDTOToEntity(ComputationalThreadsEtlDTO computationalThreadsDTO) {
        if (computationalThreadsDTO == null) {
            return null;
        } else if (computationalThreadsDTO.getId() == null) {
            ComputationalThreadsEtl user = new ComputationalThreadsEtl();
            user.setComputationalThread(computationalThreadsRepository.findOne(computationalThreadsDTO.getComputationalThread().getId()));
            user.setEtl(etlService.findOne(computationalThreadsDTO.getEtl().getId()));
            user.setExecutionOrder(computationalThreadsDTO.getExecutionOrder());
            return user;
        } else {
            return computationalThreadsEtlMapper.toEntity(computationalThreadsDTO);
        }
    }

    public List<ComputationalThreadsEtlDTO> getStatusList(List<ComputationalThreadsEtlDTO> prueba) {
        return Stream.of(prueba).flatMap(statusList -> statusList.stream()).collect(Collectors.toList());
    }

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

        baseDto.setComputationalThreadsEtl(entity.getComputationalThreadsEtl().stream().map(e -> computationalThreadsEtlMapper.toDto(e)).collect(Collectors.toList()));

        return baseDto;
    }

    public ComputationalThreadsBaseDTO toBaseDto(ComputationalThreads entity, String lastExecutionStartDate, String lastExecutionResult) {
        return toBaseDto(entity, null);
    }

}
