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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.coetl.config.AuditConstants;
import es.gobcan.istac.coetl.config.audit.AuditEventPublisher;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.ComputationalThreadsService;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsDTO;
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
    private static final Logger LOGGGER = LoggerFactory.getLogger(ComputationalThreadsResource.class);

    private final ComputationalThreadsService computationalThreadsService;
    private final ComputationalThreadsMapper computationalThreadsMapper;
    private final AuditEventPublisher auditEventPublisher;

    public ComputationalThreadsResource(ComputationalThreadsService computationalThreadsService, ComputationalThreadsMapper computationalThreadsMapper,
            AuditEventPublisher auditEventPublisher) {
        this.computationalThreadsService = computationalThreadsService;
        this.computationalThreadsMapper = computationalThreadsMapper;
        this.auditEventPublisher = auditEventPublisher;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> create(@Valid @RequestBody ComputationalThreadsDTO computationalThreadsDTO) throws URISyntaxException {
        LOGGGER.debug("REST Request to create an Computational Thread : {}", computationalThreadsDTO);
        if (computationalThreadsDTO.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new Computational Thread must not have an ID")).build();
        }

        ComputationalThreads createdComputationalThread = computationalThreadsService.create(computationalThreadsMapper.toEntity(computationalThreadsDTO));

        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(createdComputationalThread);
        auditEventPublisher.publish(AuditConstants.ETL_CREATED, result.getCode());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(COMPUTATIONAL_THREAD_ENTITY_NAME, result.getId().toString())).body(result);
    }

    @GetMapping("/{idThread}")
    @Timed
    @PreAuthorize("@secChecker.canReadEtl(authentication)")
    public ResponseEntity<ComputationalThreadsDTO> findOne(@PathVariable Long idThread) {
        LOGGGER.debug("REST Request to find an Computational Thread : {}", idThread);
        ComputationalThreads computationalThread = computationalThreadsService.findOne(idThread);
        ComputationalThreadsDTO result = computationalThreadsMapper.toDto(computationalThread);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.canManageEtl(authentication)")
    public ResponseEntity<List<ComputationalThreadsBaseDTO>> findAll(@ApiParam(required = false) String query, @ApiParam(required = false) boolean includeDeleted, @ApiParam Pageable pageable,
            @RequestParam("lastExecution") String lastExecutionStartDate, @RequestParam("lastExecutionByResult") String lastExecutionResult) {
        LOGGGER.debug("REST Request to find all Computational Threads by query : {} and including deleted : {}", query, includeDeleted);

        Page<ComputationalThreadsBaseDTO> page = computationalThreadsService.findAll(query, includeDeleted, pageable, lastExecutionStartDate, lastExecutionResult)
                .map(e -> computationalThreadsMapper.toBaseDto(e, lastExecutionStartDate, lastExecutionResult));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URI);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
