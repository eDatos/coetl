package es.gobcan.istac.coetl.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import es.gobcan.istac.coetl.domain.ExternalItem;
import es.gobcan.istac.coetl.web.rest.dto.ExternalItemDTO;

public interface ExternalItemService {

    public ExternalItem save(ExternalItem externalItem);

    public List<ExternalItemDTO> findOperations(Pageable pageable, String query);

}
