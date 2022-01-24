package es.gobcan.istac.coetl.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import es.gobcan.istac.coetl.config.AuditConstants;
import es.gobcan.istac.coetl.config.audit.AuditEventPublisher;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.pentaho.service.PentahoGitService;
import es.gobcan.istac.coetl.service.EtlService;
import es.gobcan.istac.coetl.service.ExecutionService;
import es.gobcan.istac.coetl.service.ParameterService;
import es.gobcan.istac.coetl.web.rest.dto.EtlBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.EtlDTO;
import es.gobcan.istac.coetl.web.rest.dto.ExecutionDTO;
import es.gobcan.istac.coetl.web.rest.dto.ParameterDTO;
import es.gobcan.istac.coetl.web.rest.mapper.EtlMapper;
import es.gobcan.istac.coetl.web.rest.mapper.ExecutionMapper;
import es.gobcan.istac.coetl.web.rest.mapper.ParameterMapper;
import es.gobcan.istac.coetl.web.rest.util.HeaderUtil;
import es.gobcan.istac.coetl.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(EtlResource.BASE_URI)
public class EtlResource extends AbstractResource {

    public static final String BASE_URI = "/api/etls";
    private static final String SLASH = "/";
    private static final String ETL_ENTITY_NAME = "etl";
    private static final String PARAMETER_ENTITY_NAME = "parameter";
    private static final String ETL_IS_DELETED_MESSAGE = "Etl %s is deleted";
    private static final Logger LOG = LoggerFactory.getLogger(EtlResource.class);

    private final EtlService etlService;
    private final EtlMapper etlMapper;
    private final ExecutionService executionService;
    private final ExecutionMapper executionMapper;
    private final ParameterService parameterService;
    private final ParameterMapper parameterMapper;
    private final AuditEventPublisher auditEventPublisher;
    private final PentahoGitService pentahoGitService;

    public EtlResource(EtlService etlService, EtlMapper etlMapper, ExecutionService executionService, ExecutionMapper executionMapper, ParameterService parameterService,
            ParameterMapper parameterMapper, AuditEventPublisher auditEventPublisher, PentahoGitService pentahoGitService) {
        this.etlService = etlService;
        this.etlMapper = etlMapper;
        this.executionService = executionService;
        this.executionMapper = executionMapper;
        this.parameterService = parameterService;
        this.parameterMapper = parameterMapper;
        this.auditEventPublisher = auditEventPublisher;
        this.pentahoGitService = pentahoGitService;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<EtlDTO> create(@Valid @RequestBody EtlDTO etlDTO) throws URISyntaxException {
        LOG.debug("REST Request to create an ETL : {}", etlDTO);
        if (etlDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ETL_ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new ETL must not have an ID")).build();
        }

        Etl createdEtl = etlService.create(etlMapper.toEntity(etlDTO));

        if (StringUtils.isNoneBlank(etlDTO.getUriRepository())) {
            String repositoryPath = pentahoGitService.cloneRepository(createdEtl);
            if (repositoryPath == null) {
                CustomExceptionUtil.throwCustomParameterizedException("An error ocurred cloning repository", ErrorConstants.ETL_CLONE_REPOSITORY);
            }
            Map<String, String> parameters = new HashMap<>();
            parameters.put("ETL_RESOURCES", repositoryPath);
            parameterService.createDefaultParameters(createdEtl, parameters);
        }

        EtlDTO result = etlMapper.toDto(createdEtl);
        auditEventPublisher.publish(AuditConstants.ETL_CREATED, result.getCode());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ETL_ENTITY_NAME, result.getId().toString())).body(result);
    }

    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<EtlDTO> update(@Valid @RequestBody EtlDTO etlDTO) {
        LOG.debug("REST Request to update an ETL : {}", etlDTO);
        if (etlDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ETL_ENTITY_NAME, ErrorConstants.ID_FALTA, "An updated ETL must have an ID")).build();
        }

        boolean repositoryGoingToChange = etlService.goingToChangeRepository(etlDTO);

        Etl currentEtl = etlMapper.toEntity(etlDTO);
        if (currentEtl.isDeleted()) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ETL_ENTITY_NAME, ErrorConstants.ENTITY_DELETED, String.format(ETL_IS_DELETED_MESSAGE, currentEtl.getId().toString()))).build();
        }

        Etl updatedEtl = etlService.update(currentEtl);

        if (repositoryGoingToChange) {
            String repositoryPath = pentahoGitService.replaceRepository(updatedEtl);
            if (repositoryPath == null) {
                CustomExceptionUtil.throwCustomParameterizedException("An error ocurred updating repository", ErrorConstants.ETL_REPLACE_REPOSITORY);
            }
        }

        EtlDTO result = etlMapper.toDto(updatedEtl);
        auditEventPublisher.publish(AuditConstants.ETL_UPDATED, result.getCode());

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result), HeaderUtil.createEntityUpdateAlert(ETL_ENTITY_NAME, result.getCode()));
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
            final String message = String.format("ETL %s is currently deleted, so can not be deleted twice", currentEtl.getCode());
            final String code = ErrorConstants.ETL_CURRENTLY_DELETED;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }

        Etl deletedEtl = etlService.delete(currentEtl);
        EtlDTO result = etlMapper.toDto(deletedEtl);
        auditEventPublisher.publish(AuditConstants.ETL_DELETED, result.getCode());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ETL_ENTITY_NAME, result.getCode())).body(result);
    }

    @PutMapping("/{idEtl}/restore")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<EtlDTO> restore(@PathVariable Long idEtl) {
        LOG.debug("REST Request to restore an ETL : {}", idEtl);
        Etl currentEtl = etlService.findOne(idEtl);
        if (currentEtl == null) {
            return ResponseEntity.notFound().build();
        }

        if (!currentEtl.isDeleted()) {
            final String message = String.format("ETL %s is not currently deleted, so you do not have anything to restore", currentEtl.getCode());
            final String code = ErrorConstants.ETL_CURRENTLY_NOT_DELETED;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }

        Etl recoveredEtl = etlService.restore(currentEtl);
        EtlDTO result = etlMapper.toDto(recoveredEtl);
        auditEventPublisher.publish(AuditConstants.ETL_RECOVERED, result.getCode());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ETL_ENTITY_NAME, result.getCode())).body(result);
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<List<EtlBaseDTO>> findAll(@ApiParam(required = false) String query, @ApiParam(required = false) boolean includeDeleted, @ApiParam Pageable pageable) {
        LOG.debug("REST Request to find all ETLs by query : {} and including deleted : {}", query, includeDeleted);
        Page<EtlBaseDTO> page = etlService.findAll(query, includeDeleted, pageable).map(etlMapper::toBaseDto);

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

    @GetMapping("/{idEtl}/execute")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<Void> execute(@PathVariable Long idEtl) {
        LOG.debug("REST Request to find an ETL : {}", idEtl);
        Etl etl = etlService.findOne(idEtl);
        if (etl == null) {
            return ResponseEntity.notFound().build();
        }
        if (!etl.isDeleted()) {
            pentahoGitService.updateRepository(etl);
            etlService.execute(etl);
            auditEventPublisher.publish(AuditConstants.ETL_EXECUTED, etl.getCode());
        } else {
            final String message = String.format("ETL %s can not be executed, it is deleted", etl.getCode());
            final String code = ErrorConstants.ETL_CURRENTLY_DELETED;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idEtl}/executions")
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<List<ExecutionDTO>> findAllExecutions(@PathVariable Long idEtl, @ApiParam Pageable pageable) {
        LOG.debug("REST Request to find a page of Executions by ETL : {}", idEtl);
        Page<ExecutionDTO> page = executionService.findAllByEtlId(idEtl, pageable).map(executionMapper::toDto);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URI + SLASH + idEtl + SLASH + "executions");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/{idEtl}/parameters")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<ParameterDTO> createParameter(@RequestBody ParameterDTO parameterDTO, @PathVariable Long idEtl) throws URISyntaxException {
        LOG.debug("REST Request to create a Parameter: {} with ETL : {}", parameterDTO, idEtl);
        Etl currentEtl = etlService.findOne(idEtl);
        if (currentEtl == null) {
            return ResponseEntity.notFound().build();
        }
        if (currentEtl.isDeleted()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ETL_ENTITY_NAME, ErrorConstants.ENTITY_DELETED, String.format(ETL_IS_DELETED_MESSAGE, idEtl.toString()))).build();
        }

        if (parameterDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(PARAMETER_ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new parameter must not have an ID")).build();
        }

        Parameter currentParameter = parameterMapper.toEntity(parameterDTO);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }
        if (!currentEtl.getId().equals(currentParameter.getEtl().getId())) {
            return ResponseEntity.notFound().build();
        }

        Parameter createdParameter = parameterService.create(currentParameter);
        ParameterDTO result = parameterMapper.toDto(createdParameter);
        auditEventPublisher.publish(AuditConstants.ETL_PARAMETER_CREATED, createdParameter.getId().toString());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getEtlId() + SLASH + "parameters" + SLASH + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(PARAMETER_ENTITY_NAME, result.getId().toString())).body(result);
    }

    @PutMapping("/{idEtl}/parameters")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<ParameterDTO> updateParameter(@RequestBody ParameterDTO parameterDTO, @PathVariable Long idEtl) {
        LOG.debug("REST Request to update a Parameter: {} with ETL : {}", parameterDTO, idEtl);
        Etl currentEtl = etlService.findOne(idEtl);
        if (currentEtl == null) {
            return ResponseEntity.notFound().build();
        }
        if (currentEtl.isDeleted()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ETL_ENTITY_NAME, ErrorConstants.ENTITY_DELETED, String.format(ETL_IS_DELETED_MESSAGE, idEtl.toString()))).build();
        }

        if (parameterDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(PARAMETER_ENTITY_NAME, ErrorConstants.ID_FALTA, "An updated parameter must have an ID")).build();
        }

        Parameter currentParameter = parameterMapper.toEntity(parameterDTO);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }
        if (!currentEtl.getId().equals(currentParameter.getEtl().getId())) {
            return ResponseEntity.notFound().build();
        }

        Parameter updatedParameter = parameterService.update(currentParameter);
        ParameterDTO result = parameterMapper.toDto(updatedParameter);
        auditEventPublisher.publish(AuditConstants.ETL_PARAMETER_UPDATED, updatedParameter.getId().toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(PARAMETER_ENTITY_NAME, result.getId().toString())).body(result);
    }

    @DeleteMapping("/{idEtl}/parameters/{parameterId}")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<Void> deleteParameterByEtlIdAndId(@PathVariable Long idEtl, @PathVariable Long parameterId) {
        LOG.debug("REST Request to delete a Parameter: {} with ETL : {}", parameterId, idEtl);
        Etl currentEtl = etlService.findOne(idEtl);
        if (currentEtl == null) {
            return ResponseEntity.notFound().build();
        }
        if (currentEtl.isDeleted()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ETL_ENTITY_NAME, ErrorConstants.ENTITY_DELETED, String.format(ETL_IS_DELETED_MESSAGE, idEtl.toString()))).build();
        }

        Parameter currentParameter = parameterService.findOneByIdAndEtlId(parameterId, idEtl);
        if (currentParameter == null) {
            return ResponseEntity.notFound().build();
        }

        parameterService.delete(currentParameter);
        auditEventPublisher.publish(AuditConstants.ETL_PARAMETER_DELETED, parameterId.toString());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(PARAMETER_ENTITY_NAME, parameterId.toString())).build();
    }

    @GetMapping("/{idEtl}/parameters")
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<List<ParameterDTO>> findAllParametersByEtlId(@PathVariable Long idEtl) {
        LOG.debug("REST Request to find all Parameters by ETL : {}", idEtl);

        List<Parameter> parameters = parameterService.findAllByEtlId(idEtl);
        List<ParameterDTO> result = parameterMapper.toDto(parameters);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{idEtl}/parameters/{parameterId}")
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<ParameterDTO> findParameterByEtlIdAndId(@PathVariable Long idEtl, @PathVariable Long parameterId) {
        LOG.debug("REST Request to find a Parameter: {} with ETL : {}", parameterId, idEtl);

        Parameter parameter = parameterService.findOneByIdAndEtlId(parameterId, idEtl);
        ParameterDTO result = parameterMapper.toDto(parameter);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
}
