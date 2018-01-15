package es.tenerife.secretaria.libro.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.tenerife.secretaria.libro.domain.PersistentAuditEvent;

public interface PersistenceAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

	List<PersistentAuditEvent> findByPrincipal(String principal);

	List<PersistentAuditEvent> findByAuditEventDateAfter(Instant after);

	List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, Instant after);

	List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfterAndAuditEventType(String principle, Instant after,
			String type);

	Page<PersistentAuditEvent> findAllByAuditEventDateBetween(Instant fromDate, Instant toDate, Pageable pageable);
}
