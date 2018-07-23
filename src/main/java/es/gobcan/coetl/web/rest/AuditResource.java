package es.gobcan.coetl.web.rest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.gobcan.coetl.service.AuditEventService;
import es.gobcan.coetl.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/management/audits")
public class AuditResource extends AbstractResource {

    private final AuditEventService auditEventService;

    public AuditResource(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @GetMapping
    @PreAuthorize("@secChecker.puedeConsultarAuditoria(authentication)")
    public ResponseEntity<List<AuditEvent>> getAll(@ApiParam Pageable pageable) {
        Page<AuditEvent> page = auditEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(params = {"fromDate", "toDate"})
    @PreAuthorize("@secChecker.puedeConsultarAuditoria(authentication)")
    public ResponseEntity<List<AuditEvent>> getByDates(@RequestParam(value = "fromDate") LocalDate fromDate, @RequestParam(value = "toDate") LocalDate toDate, @ApiParam Pageable pageable) {

        Page<AuditEvent> page = auditEventService.findByDates(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/management/audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id:.+}")
    @PreAuthorize("@secChecker.puedeConsultarAuditoria(authentication)")
    public ResponseEntity<AuditEvent> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(auditEventService.find(id));
    }
}
