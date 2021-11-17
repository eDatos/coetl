package es.gobcan.istac.coetl.web.rest.mapper;

import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.ExternalItem;
import es.gobcan.istac.coetl.repository.ExternalItemRepository;
import es.gobcan.istac.coetl.web.rest.dto.ExternalItemDTO;

@Mapper(componentModel = "spring", uses = {})
public abstract class ExternalItemMapper implements EntityMapper<ExternalItemDTO, ExternalItem> {

    @Autowired
    private ExternalItemRepository externalItemRepository;

    public ExternalItem fromId(Long id) {
        return externalItemRepository.findOne(id);
    }
    public ExternalItem fromCode(String code) {
        return externalItemRepository.findByCode(code);
    }

    @Override
    public ExternalItemDTO toDto(ExternalItem entity) {
        if (entity == null) {
            return null;
        }

        ExternalItemDTO externalItemDTO = new ExternalItemDTO();

        externalItemDTO.setId(entity.getId());
        externalItemDTO.setCode(entity.getCode());
        externalItemDTO.setUrn(entity.getUrn());
        externalItemDTO.setName(entity.getName());
        externalItemDTO.setUri(entity.getUri());
        externalItemDTO.setUrnProvider(entity.getUrnProvider());
        externalItemDTO.setManagementAppUrl(entity.getManagementAppUrl());
        externalItemDTO.setType(entity.getType());

        return externalItemDTO;
    }

    public ExternalItemDTO toDtoFromOperation(ResourceInternal resourceInternal, TypeExternalArtefactsEnum type) {
        if (resourceInternal == null) {
            return null;
        }

        ExternalItem entity = StringUtils.isNotEmpty(resourceInternal.getId()) ? fromCode(resourceInternal.getId()) : null;

        ExternalItemDTO externalItemDTO = new ExternalItemDTO();

        externalItemDTO.setId(entity != null ? entity.getId() : null);
        externalItemDTO.setCode(resourceInternal.getId());
        String name = resourceInternal.getName().getTexts().stream()
            .filter(localisedString -> "es".equalsIgnoreCase(localisedString.getLang()))
            .map(localisedString -> localisedString.getValue()).findFirst().get();
        externalItemDTO.setUrn(resourceInternal.getUrn());
        externalItemDTO.setName(name);
        externalItemDTO.setUri(resourceInternal.getSelfLink().getHref());
        externalItemDTO.setManagementAppUrl(resourceInternal.getManagementAppLink());
        externalItemDTO.setType(type);

        return externalItemDTO;
    }

    @Override
    public ExternalItem toEntity(ExternalItemDTO dto) {
        if (dto == null) {
            return null;
        }

        ExternalItem entity = (dto.getId() != null) ? fromId(dto.getId()) : new ExternalItem();

        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setUrn(dto.getUrn());
        entity.setUri(dto.getUri());
        entity.setUrnProvider(dto.getUrnProvider());
        entity.setManagementAppUrl(dto.getManagementAppUrl());
        entity.setType(dto.getType());

        return entity;
    }
}
