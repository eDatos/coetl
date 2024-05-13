package es.gobcan.istac.coetl.web.rest;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.coetl.config.AuditConstants;
import es.gobcan.istac.coetl.config.audit.AuditEventPublisher;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.service.ComputationalThreadExecutionService;
import es.gobcan.istac.coetl.service.ComputationalThreadsService;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadExecutionDTO;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsDTO;
import es.gobcan.istac.coetl.web.rest.mapper.ComputationalThreadsExecutionMapper;
import es.gobcan.istac.coetl.web.rest.mapper.ComputationalThreadsMapper;
import es.gobcan.istac.coetl.web.rest.util.HeaderUtil;
import es.gobcan.istac.coetl.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(ComputationalThreadsResource.BASE_URI)
public class ComputationalThreadsResource extends AbstractResource {

    public static final String BASE_URI = "/api/computational-threads";
    private static final String SLASH = "/";
    private static final String COMPUTATIONAL_THREAD_ENTITY_NAME = "computational-threads";
    private static final String COMPUTATIONAL_THREAD_IS_DELETED_MESSAGE = "Computational Thread %s is deleted";
    private static final Logger LOGGGER = LoggerFactory.getLogger(ComputationalThreadsResource.class);

    private final ComputationalThreadsService computationalThreadsService;
    private final ComputationalThreadExecutionService computationalThreadExecutionService;
    private final ComputationalThreadsMapper computationalThreadsMapper;
    private final AuditEventPublisher auditEventPublisher;
    private final ComputationalThreadsExecutionMapper computationalThreadsExecutionMapper;

    public ComputationalThreadsResource(ComputationalThreadsService computationalThreadsService, ComputationalThreadsMapper computationalThreadsMapper,
            AuditEventPublisher auditEventPublisher, ComputationalThreadsExecutionMapper computationalThreadsExecutionMapper,
            ComputationalThreadExecutionService computationalThreadExecutionService) {
        this.computationalThreadsService = computationalThreadsService;
        this.computationalThreadsMapper = computationalThreadsMapper;
        this.auditEventPublisher = auditEventPublisher;
        this.computationalThreadsExecutionMapper = computationalThreadsExecutionMapper;
        this.computationalThreadExecutionService = computationalThreadExecutionService;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.canManageComputationalThread(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> create(@Valid @RequestBody ComputationalThreadsDTO computationalThreadsDTO) throws URISyntaxException {
        LOGGGER.debug("REST Request to create an Computational Thread : {}", computationalThreadsDTO);
        if (computationalThreadsDTO.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new Computational Thread must not have an ID")).build();
        }

        ComputationalThreads createdComputationalThread = computationalThreadsService.create(computationalThreadsMapper.toEntity(computationalThreadsDTO));

        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(createdComputationalThread);
        auditEventPublisher.publish(AuditConstants.COMPUTATIONAL_THREAD_CREATED, result.getCode());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, result.getId().toString())).body(result);
    }

    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.canManageComputationalThread(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> update(@Valid @RequestBody ComputationalThreadsDTO computationalThreadsDTO) {
        LOGGGER.debug("REST Request to update an Computational Thread : {}", computationalThreadsDTO);
        if (computationalThreadsDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ID_FALTA, "An updated ETL must have an ID")).build();
        }

        ComputationalThreads currentComputationalThread = computationalThreadsMapper.toEntity(computationalThreadsDTO);
        if (currentComputationalThread.isDeleted()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ENTITY_DELETED,
                    String.format(COMPUTATIONAL_THREAD_IS_DELETED_MESSAGE, currentComputationalThread.getId().toString()))).build();
        }

        ComputationalThreads updatedEtl = computationalThreadsService.update(currentComputationalThread);

        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(updatedEtl);
        auditEventPublisher.publish(AuditConstants.COMPUTATIONAL_THREAD_UPDATED, result.getCode());

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result), HeaderUtil.createEntityUpdateAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, result.getCode()));
    }

    @DeleteMapping("/{idThread}")
    @Timed
    @PreAuthorize("@secChecker.canManageComputationalThread(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> delete(@PathVariable Long idThread) {
        LOGGGER.debug("REST Request to delete an Computational Thread : {}", idThread);
        if (idThread == null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ID_FALTA, "Cannot delete a Computational Thread ID not found"))
                    .build();
        }
        ComputationalThreads computationalThread = computationalThreadsService.findOne(idThread);
        if (computationalThread == null) {
            return ResponseEntity.notFound().build();
        }
        if (computationalThread.isDeleted()) {
            final String message = String.format("Computational Thread %s is currently deleted, so can not be deleted twice", computationalThread.getCode());
            final String code = ErrorConstants.COMPUTATIONAL_THREAD_CURRENTLY_DELETED;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }
        ComputationalThreads deletedThread = computationalThreadsService.delete(computationalThread);
        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(deletedThread);
        auditEventPublisher.publish(AuditConstants.COMPUTATIONAL_THREAD_DELETED, result.getCode());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, result.getCode())).body(result);
    }

    @PutMapping("/{idThread}/restore")
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> restore(@PathVariable Long idThread) {
        LOGGGER.debug("REST Request to restore an Computational Thread : {}", idThread);
        if (idThread == null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ID_FALTA, "Cannot delete a Computational Thread ID not found"))
                    .build();
        }
        ComputationalThreads computationalThread = computationalThreadsService.findOne(idThread);
        if (computationalThread == null) {
            return ResponseEntity.notFound().build();
        }

        if (!computationalThread.isDeleted()) {
            final String message = String.format("Computational thread %s is not currently deleted, so you do not have anything to restore", computationalThread.getCode());
            final String code = ErrorConstants.COMPUTATIONAL_THREAD_CURRENTLY_NOT_DELETED;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }

        ComputationalThreads recoveredEtl = computationalThreadsService.restore(computationalThread);
        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(recoveredEtl);
        auditEventPublisher.publish(AuditConstants.COMPUTATIONAL_THREAD_RECOVERED, result.getCode());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, result.getCode())).body(result);
    }

    @GetMapping("/{idThread}")
    @Timed
    @PreAuthorize("@secChecker.canReadComputationalThread(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> findOne(@PathVariable Long idThread) {
        LOGGGER.debug("REST Request to find an Computational Thread : {}", idThread);
        ComputationalThreads computationalThread = computationalThreadsService.findOne(idThread);
        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(computationalThread);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.canManageComputationalThread(authentication)")
    public ResponseEntity<List<ComputationalThreadsBaseDTO>> findAll(@ApiParam(required = false) String query, @ApiParam(required = false) boolean includeDeleted, @ApiParam Pageable pageable,
            @RequestParam("lastExecution") String lastExecutionStartDate, @RequestParam("lastExecutionByResult") String lastExecutionResult) {
        LOGGGER.debug("REST Request to find all Computational Threads by query : {} and including deleted : {}", query, includeDeleted);

        Page<ComputationalThreadsBaseDTO> page = computationalThreadsService.findAll(query, includeDeleted, pageable, lastExecutionStartDate, lastExecutionResult)
                .map(e -> computationalThreadsMapper.toBaseDto(e, lastExecutionStartDate, lastExecutionResult));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URI);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/{idThread}/create-execution")
    @Timed
    @PreAuthorize("@secChecker.canManageComputationalThread(authentication)")
    public ResponseEntity<ComputationalThreadExecutionDTO> createExecution(@PathVariable Long idThread, @RequestBody ComputationalThreadExecutionDTO computationalThreadExecutionDTO)
            throws URISyntaxException {
        LOGGGER.debug("REST Request to create a new Computational Thread Execution to thread: {}", idThread);
        if (idThread == null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ID_FALTA, "A new Computational Thread Execution must have an ID of thread"))
                    .build();
        }

        String executor = SecurityContextHolder.getContext().getAuthentication().getName();
        ComputationalThreadExecution threadExecutionToEntity = computationalThreadsExecutionMapper.toEntity(computationalThreadExecutionDTO);
        ComputationalThreadExecution newThreadExecution = computationalThreadsService.createThreadExecution(threadExecutionToEntity, executor);
        computationalThreadsService.executeThread(newThreadExecution, executor);
        auditEventPublisher.publish(AuditConstants.COMPUTATIONAL_THREAD_EXECUTED, newThreadExecution.getComputationalThread().getCode());
        ComputationalThreadExecutionDTO result = computationalThreadsExecutionMapper.toDto(newThreadExecution);
        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getIdThread() + SLASH + "create-execution" + SLASH + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, result.getId().toString())).body(result);
    }

    @GetMapping("/{idThread}/executions")
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<List<ComputationalThreadExecutionDTO>> findAllExecutions(@PathVariable Long idThread, @ApiParam Pageable pageable) {
        LOGGGER.debug("REST Request to find a page of Executions by Computational Thread : {}", idThread);
        Page<ComputationalThreadExecutionDTO> page = computationalThreadExecutionService.findAllByComputationalThreadId(idThread, pageable)
                .map(computationalThreadsExecutionMapper::toDto);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URI + SLASH + idThread + SLASH + "executions");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
