package es.gobcan.istac.coetl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.web.rest.dto.EtlDTO;

public interface EtlService {

    public Etl create(Etl etl);
    public Etl update(Etl etl);
    public Etl delete(Etl etl);
    public Etl restore(Etl etl);
    public Etl findOne(Long id);
    public Page<Etl> findAll(String query, boolean includeDeleted, Pageable pageable);
    public void execute(Etl etl);

    public boolean goingToChangeRepository(EtlDTO etlDto);
}
