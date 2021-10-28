package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.ExternalItem;

@Repository
public interface ExternalItemRepository extends JpaRepository<ExternalItem, Long> {

    public ExternalItem findByCode(String code);
}
