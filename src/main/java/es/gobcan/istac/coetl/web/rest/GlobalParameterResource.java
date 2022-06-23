package es.gobcan.istac.coetl.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.gobcan.istac.coetl.config.AuditConstants;
import es.gobcan.istac.coetl.config.audit.AuditEventPublisher;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.ParameterService;
import es.gobcan.istac.coetl.web.rest.dto.ParameterDTO;
import es.gobcan.istac.coetl.web.rest.mapper.ParameterMapper;
import es.gobcan.istac.coetl.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(GlobalParameterResource.BASE_URI)
public class GlobalParameterResource extends AbstractResource {

    public static final String BASE_URI = "/api/global-parameters";
    private static final String ENTITY_NAME = "parameter";
    private static final Logger LOG = LoggerFactory.getLogger(GlobalParameterResource.class);

    private final ParameterService parameterService;
    private final ParameterMapper parameterMapper;
    private final AuditEventPublisher auditEventPublisher;

    public GlobalParameterResource(ParameterService parameterService, ParameterMapper parameterMapper, AuditEventPublisher auditEventPublisher) {
        this.parameterService = parameterService;
        this.parameterMapper = parameterMapper;
        this.auditEventPublisher = auditEventPublisher;
    }

    @PostMapping()
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<ParameterDTO> createGlobalParameter(@RequestBody ParameterDTO parameterDTO) throws URISyntaxException {
        LOG.debug("REST Request to create a Global Parameter: {} ", parameterDTO);

        if (parameterDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new parameter must not have an ID")).build();
        }

        Parameter currentParameter = parameterMapper.toEntity(parameterDTO);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        Parameter createdParameter = parameterService.create(currentParameter);
        ParameterDTO result = parameterMapper.toDto(createdParameter);

        auditEventPublisher.publish(AuditConstants.GLOBAL_PARAMETER_CREATED, createdParameter.getId().toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    @PutMapping()
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<ParameterDTO> updateGlobalParameter(@RequestBody ParameterDTO parameterDTO) {
        LOG.debug("REST Request to update a Global Parameter: {}", parameterDTO);
        if (parameterDTO.getId() == null) {
            return ResponseEntity.notFound().build();
        }

        Parameter currentParameter = parameterMapper.toEntity(parameterDTO);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        Parameter updatedParameter = parameterService.update(currentParameter);
        ParameterDTO result = parameterMapper.toDto(updatedParameter);
        auditEventPublisher.publish(AuditConstants.GLOBAL_PARAMETER_UPDATED, updatedParameter.getId().toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    @GetMapping()
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<List<ParameterDTO>> findAllGlobalParameters(@ApiParam Pageable pageable) {
        LOG.debug("REST Request to find all Global Parameter ");

        Page<ParameterDTO> page = parameterService.findAllGlobalParameters(pageable).map(parameterMapper::toDto);

        return ResponseEntity.ok().body(page.getContent());
    }

    @DeleteMapping("/{parameterId}")
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<Void> deleteGlobalParameter(@PathVariable Long parameterId) {
        LOG.debug("REST Request to delete a Parameter: {} with ETL : {}", parameterId);

        Parameter currentParameter = parameterService.findOneById(parameterId);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        parameterService.delete(currentParameter);
        auditEventPublisher.publish(AuditConstants.GLOBAL_PARAMETER_DELETED, parameterId.toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, parameterId.toString())).build();
    }


    @GetMapping("/{parameterId}/decode")
    @Timed
    @PreAuthorize("@secChecker.canManageGlobalParameters(authentication)")
    public ResponseEntity<ParameterDTO> decodeGlobalParameter(@PathVariable Long parameterId) {
        LOG.debug("REST Request to decode value of Parameter: {} ", parameterId);

        Parameter parameter = parameterService.findOneById(parameterId);
        ParameterDTO result = parameterMapper.toDto(parameter);
        result.setValue(parameterService.decodeValueByTypology(parameter));

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
