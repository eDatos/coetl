package es.gobcan.istac.coetl.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.gobcan.istac.coetl.config.AuditConstants;
import es.gobcan.istac.coetl.config.audit.AuditEventPublisher;
import es.gobcan.istac.coetl.domain.GlobalParameter;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.GlobalParameterService;
import es.gobcan.istac.coetl.web.rest.dto.GlobalParameterDTO;
import es.gobcan.istac.coetl.web.rest.mapper.GlobalParameterMapper;
import es.gobcan.istac.coetl.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(GlobalParameterResource.BASE_URI)
public class GlobalParameterResource extends AbstractResource {

    public static final String BASE_URI = "/api/global-parameters";
    private static final String ENTITY_NAME = "global_parameter";
    private static final Logger LOG = LoggerFactory.getLogger(GlobalParameterResource.class);

    private final GlobalParameterService globalParameterService;
    private final GlobalParameterMapper globalParameterMapper;
    private final AuditEventPublisher auditEventPublisher;


    public GlobalParameterResource(GlobalParameterService globalParameterService, GlobalParameterMapper globalParameterMapper, AuditEventPublisher auditEventPublisher) {
        this.globalParameterService = globalParameterService;
        this.globalParameterMapper = globalParameterMapper;
        this.auditEventPublisher = auditEventPublisher;
    }

    @PostMapping()
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<GlobalParameterDTO> createGlobalParameter(@RequestBody GlobalParameterDTO globalParameterDTO) throws URISyntaxException {
        LOG.debug("REST Request to create a Global Parameter: {} ", globalParameterDTO);

        if (globalParameterDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new parameter must not have an ID")).build();
        }

        GlobalParameter currentParameter = globalParameterMapper.toEntity(globalParameterDTO);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        GlobalParameter createdParameter = globalParameterService.create(currentParameter);
        GlobalParameterDTO result = globalParameterMapper.toDto(createdParameter);

        auditEventPublisher.publish(AuditConstants.GLOBAL_PARAMETER_CREATED, createdParameter.getId().toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    @PutMapping()
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<GlobalParameterDTO> updateGlobalParameter(@RequestBody GlobalParameterDTO gloablParameterDTO) {
        LOG.debug("REST Request to update a Global Parameter: {}", gloablParameterDTO);
        if (gloablParameterDTO.getId() == null) {
            return ResponseEntity.notFound().build();
        }

        GlobalParameter currentParameter = globalParameterMapper.toEntity(gloablParameterDTO);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        GlobalParameter updatedParameter = globalParameterService.update(currentParameter);
        GlobalParameterDTO result = globalParameterMapper.toDto(updatedParameter);
        auditEventPublisher.publish(AuditConstants.GLOBAL_PARAMETER_UPDATED, updatedParameter.getId().toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    @GetMapping()
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<List<GlobalParameterDTO>> findAllGlobalParameters(@ApiParam Pageable pageable) {
        LOG.debug("REST Request to find all Global Parameter ");

        Page<GlobalParameterDTO> page = globalParameterService.findAll(pageable).map(globalParameterMapper::toDto);

        return ResponseEntity.ok().body(page.getContent());
    }

    @DeleteMapping("/{parameterId}")
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<Void> deleteGlobalParameter(@PathVariable Long parameterId) {
        LOG.debug("REST Request to delete a Global Parameter: {} with ETL : {}", parameterId);

        GlobalParameter currentParameter = globalParameterService.findOneById(parameterId);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        globalParameterService.delete(currentParameter);
        auditEventPublisher.publish(AuditConstants.GLOBAL_PARAMETER_DELETED, parameterId.toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, parameterId.toString())).build();
    }


    @GetMapping("/{parameterId}/decode")
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<GlobalParameterDTO> decodeGlobalParameter(@PathVariable Long parameterId) {
        LOG.debug("REST Request to decode value of Global Parameter: {} ", parameterId);

        GlobalParameter parameter = globalParameterService.findOneById(parameterId);
        GlobalParameterDTO result = globalParameterMapper.toDto(parameter);
        result.setValue(globalParameterService.decodeValueByTypology(parameter));

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
