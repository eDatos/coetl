package es.gobcan.istac.coetl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.ExternalItem;
import es.gobcan.istac.coetl.repository.ExternalItemRepository;
import es.gobcan.istac.coetl.service.ExternalItemService;

@Service
public class ExternalItemServiceImpl implements ExternalItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalItemService.class);

    @Autowired
    private ExternalItemRepository externalItemRepository;

    @Override
    public ExternalItem save(ExternalItem externalItem) {
        LOG.debug("Request to save an externalItem : {}", externalItem);
        return externalItemRepository.saveAndFlush(externalItem);
    }
}
