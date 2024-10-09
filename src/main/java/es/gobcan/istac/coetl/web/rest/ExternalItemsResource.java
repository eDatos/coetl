package es.gobcan.istac.coetl.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.service.ExternalItemService;
import es.gobcan.istac.coetl.web.rest.dto.ExternalItemDTO;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(ExternalItemsResource.BASE_URI)
public class ExternalItemsResource extends AbstractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalItemsResource.class);
    public static final String BASE_URI = "/api/external-item";

    private final ExternalItemService externalItemService;

    public ExternalItemsResource(ExternalItemService externalItemService) {
        this.externalItemService = externalItemService;
    }

    @GetMapping("/statistical-operations")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<List<ExternalItemDTO>> getList(@ApiParam Pageable pageable, @RequestParam(required = false) String query) {
        List<String> codes = SecurityUtils.statisticalOperationsCodesAllowed().stream().collect(Collectors.toList());
        LOG.debug("REST Request to find all statistical operations by query : {} and including deleted : {}");
        List<ExternalItemDTO> result =  externalItemService.findOperations(pageable, query);

        if (!SecurityUtils.isAdmin() && codes != null && !codes.isEmpty()) {
            List<ExternalItemDTO> externalItemsAllowed = new ArrayList<>();
            externalItemsAllowed.addAll(result.stream().filter(item -> codes.contains(item.getCode())).collect(Collectors.toList()));
            return ResponseEntity.ok().body(externalItemsAllowed);
        }

        return ResponseEntity.ok().body(result);
    }
}
