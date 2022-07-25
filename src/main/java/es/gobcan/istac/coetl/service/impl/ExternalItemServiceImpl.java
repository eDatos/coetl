package es.gobcan.istac.coetl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.ExternalItem;
import es.gobcan.istac.coetl.invocation.facade.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.coetl.repository.ExternalItemRepository;
import es.gobcan.istac.coetl.service.ExternalItemService;
import es.gobcan.istac.coetl.web.rest.dto.ExternalItemDTO;
import es.gobcan.istac.coetl.web.rest.mapper.ExternalItemMapper;

@Service
public class ExternalItemServiceImpl implements ExternalItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalItemService.class);

    @Autowired
    private ExternalItemRepository externalItemRepository;
    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;
    @Autowired
    private ExternalItemMapper externalItemMapper;

    @Override
    public ExternalItem save(ExternalItem externalItem) {
        LOG.debug("Request to save an externalItem : {}", externalItem);
        return externalItemRepository.saveAndFlush(externalItem);
    }

    @Override
    public List<ExternalItemDTO> findOperations(Pageable pageable, String query) {

        List<ExternalItemDTO> result =  statisticalOperationsRestInternalFacade.findOperations(pageable.getPageSize(),pageable.getPageNumber(),query)
            .getOperations().stream()
            .map( operation -> externalItemMapper.toDtoFromOperation(operation, TypeExternalArtefactsEnum.STATISTICAL_OPERATION))
            .collect(Collectors.toList());

        return  result;
    }
}
