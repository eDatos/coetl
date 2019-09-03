package es.gobcan.istac.coetl.service;

import java.util.List;

import es.gobcan.istac.coetl.domain.Parameter;

public interface ParameterService {

    public Parameter create(Parameter parameter);
    public Parameter update(Parameter parameter);
    public void delete(Parameter parameter);
    public List<Parameter> findAllByEtlId(Long etlId);
    public Parameter findOneByIdAndEtlId(Long id, Long etlId);
}
