package es.gobcan.istac.coetl.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import es.gobcan.istac.coetl.domain.Health;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.HealthService;
import es.gobcan.istac.coetl.web.rest.dto.HealthDTO;
import es.gobcan.istac.coetl.web.rest.mapper.HealthMapper;
import es.gobcan.istac.coetl.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping(HealthResource.BASE_URI)
public class HealthResource extends AbstractResource {

    public static final String BASE_URI = "/api/health";
    private static final String SLASH = "/";
    private static final String ENTITY_NAME = "health";
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthResource.class);

    private final HealthService healthService;
    private final HealthMapper healthMapper;
    private final AuditEventPublisher auditEventPublisher;

    public HealthResource(HealthService healthService, HealthMapper healthMapper, AuditEventPublisher auditEventPublisher) {
        this.healthService = healthService;
        this.healthMapper = healthMapper;
        this.auditEventPublisher = auditEventPublisher;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.puedeGestionarSalud(authentication)")
    public ResponseEntity<HealthDTO> create(@Valid @RequestBody HealthDTO healthDTO) throws URISyntaxException {
        LOGGER.debug("REST Request to create a Health Indicator : {}", healthDTO);
        if (healthDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new Health Indicator must not have an ID")).body(null);
        }

        Health createdHealth = healthService.create(healthMapper.toEntity(healthDTO));
        HealthDTO result = healthMapper.toDto(createdHealth);
        auditEventPublisher.publish(AuditConstants.HEALTH_CREATED, result.getServiceName());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.puedeGestionarSalud(authentication)")
    public ResponseEntity<HealthDTO> update(@Valid @RequestBody HealthDTO healthDTO) throws URISyntaxException {
        LOGGER.debug("REST Request to update a Health Indicator : {}", healthDTO);
        if (healthDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_EXISTE, "A new Health Indicator must not have an ID")).body(null);
        }

        Health updatedHealth = healthService.update(healthMapper.toEntity(healthDTO));
        HealthDTO result = healthMapper.toDto(updatedHealth);
        auditEventPublisher.publish(AuditConstants.HEALTH_UPDATED, result.getServiceName());

        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId())).headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarSalud(authentication)")
    public ResponseEntity<List<HealthDTO>> findAll() {
        LOGGER.debug("REST Request to find all Health Indicators");
        List<Health> healths = healthService.findAll();
        List<HealthDTO> result = healthMapper.toDto(healths);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idHealth}")
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarSalud(authentication)")
    public ResponseEntity<HealthDTO> findOne(@PathVariable Long idHealth) {
        LOGGER.debug("REST Request to find a Health Indicator : {}", idHealth);
        Health health = healthService.findOne(idHealth);
        HealthDTO result = healthMapper.toDto(health);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @DeleteMapping("/{idHealth}")
    @Timed
    @PreAuthorize("@secChecker.puedeGestionarSalud(authentication)")
    public ResponseEntity<Void> delete(@PathVariable Long idHealth) {
        LOGGER.debug("REST Request to delete a Health Indicator : {}", idHealth);
        Health currentHealth = healthService.findOne(idHealth);
        if (currentHealth == null) {
            return ResponseEntity.notFound().build();
        }

        healthService.delete(currentHealth);
        auditEventPublisher.publish(AuditConstants.HEALTH_DELETED, currentHealth.getServiceName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    @Timed
    @PreAuthorize("@secChecker.puedeConsultarSalud(authentication)")
    public ResponseEntity<Map<String, Object>> check() {
        LOGGER.debug("REST Request to check Health Indicators");
        Map<String, Object> checkedHealth = healthService.check();

        return ResponseEntity.ok(checkedHealth);
    }
}