package es.gobcan.coetl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Type;

public interface ExecutionService {

    public Execution create(Etl etl, Type type);
    public Page<Execution> findAllByEtlId(Long idEtl, Pageable pageable);
}
