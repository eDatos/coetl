package es.gobcan.istac.coetl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;

public interface ExecutionService {

    public Execution create(Execution execution);
    public Execution update(Execution execution);
    public Page<Execution> findAllByEtlId(Long idEtl, Pageable pageable);
    public boolean existsRunnnigOrWaitingByEtl(Long idEtl);
    public List<Execution> getInRunningResultAndEtlExecutionPlatform(TipoPlataformaEjecucion platform);
    public Execution getOldestInWaitingResultAndEtlExecutionPlatform(TipoPlataformaEjecucion platform);
}
