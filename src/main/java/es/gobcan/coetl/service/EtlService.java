package es.gobcan.coetl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.coetl.domain.Etl;

public interface EtlService {

    public Etl create(Etl etl);
    public Etl update(Etl etl);
    public Etl delete(Etl etl);
    public Etl restore(Etl etl);
    public Etl findOne(Long id);
    public Page<Etl> findAll(String query, boolean includeDeleted, Pageable pageable);
}
