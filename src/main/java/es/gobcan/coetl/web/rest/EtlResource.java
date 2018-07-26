package es.gobcan.coetl.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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

import com.codahale.metrics.annotation.Timed;

import es.gobcan.coetl.config.AuditConstants;
import es.gobcan.coetl.config.audit.AuditEventPublisher;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.service.EtlService;
import es.gobcan.coetl.web.rest.dto.EtlDTO;
import es.gobcan.coetl.web.rest.mapper.EtlMapper;
import es.gobcan.coetl.web.rest.util.HeaderUtil;
import es.gobcan.coetl.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(EtlResource.BASE_URI)
public class EtlResource extends AbstractResource {

    public static final String BASE_URI = "/api/etls";
    private static final String SLASH = "/";
    private static final String ENTITY_NAME = "etl";
    private static final Logger LOG = LoggerFactory.getLogger(EtlResource.class);

    private final EtlService etlService;
    private final EtlMapper etlMapper;
    private final AuditEventPublisher auditEventPublisher;

    public EtlResource(EtlService etlService, EtlMapper etlMapper, AuditEventPublisher auditEventPublisher) {
        this.etlService = etlService;
        this.etlMapper = etlMapper;
        this.auditEventPublisher = auditEventPublisher;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<EtlDTO> create(@Valid @RequestBody EtlDTO etlDTO) throws URISyntaxException {
        LOG.debug("REST Request to create an ETL : {}", etlDTO);
        if (etlDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new ETL must not have an ID")).body(null);
        }

        Etl createdEtl = etlService.create(etlMapper.toEntity(etlDTO));
        EtlDTO result = etlMapper.toDto(createdEtl);
        auditEventPublisher.publish(AuditConstants.ETL_CREATED, result.getCode());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId())).headers(HeaderUtil.createAlert("coetlApp.etl.created", result.getCode())).body(result);
    }

    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<EtlDTO> update(@Valid @RequestBody EtlDTO etlDTO) {
        LOG.debug("REST Request to update an ETL : {}", etlDTO);
        if (etlDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_FALTA, "An updated ETL must have an ID")).body(null);
        }

        Etl updatedEtl = etlService.update(etlMapper.toEntity(etlDTO));
        EtlDTO result = etlMapper.toDto(updatedEtl);
        auditEventPublisher.publish(AuditConstants.ETL_UPDATED, result.getCode());

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result), HeaderUtil.createAlert("coetlApp.etl.updated", result.getCode()));
    }

    @DeleteMapping("/{idEtl}")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<EtlDTO> delete(@PathVariable Long idEtl) {
        LOG.debug("REST Request to delete an ETL : {}", idEtl);
        Etl currentEtl = etlService.findOne(idEtl);
        if (currentEtl == null) {
            return ResponseEntity.notFound().build();
        }

        if (currentEtl.isDeleted()) {
            throw new CustomParameterizedExceptionBuilder().message(String.format("ETL %s is currently deleted, so can not be deleted twice", currentEtl.getCode()))
                    .code(ErrorConstants.ETL_CURRENTLY_DELETED).build();
        }

        Etl deletedEtl = etlService.delete(currentEtl);
        EtlDTO result = etlMapper.toDto(deletedEtl);
        auditEventPublisher.publish(AuditConstants.ETL_DELETED, result.getCode());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<List<EtlDTO>> findAll(@ApiParam(required = false) String query, @ApiParam(required = false) boolean includeDeleted, @ApiParam Pageable pageable) {
        LOG.debug("REST Request to find all ETLs by query : {} and including deleted : {}", query, includeDeleted);
        Page<EtlDTO> page = etlService.findAll(query, includeDeleted, pageable).map(etlMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URI);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{idEtl}")
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<EtlDTO> findOne(@PathVariable Long idEtl) {
        LOG.debug("REST Request to find an ETL : {}", idEtl);
        Etl etl = etlService.findOne(idEtl);
        EtlDTO result = etlMapper.toDto(etl);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
