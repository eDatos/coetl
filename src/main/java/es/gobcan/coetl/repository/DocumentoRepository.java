package es.gobcan.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

}
