package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Parameter;

import java.util.List;
import java.util.Map;

public interface ParameterService {

    public List<Parameter> createDefaultParameters(Etl etl, Map<String, String> parameters);
    public void deleteDefaultParameters(Etl etl);
    public Parameter create(Parameter parameter);
    public Parameter update(Parameter parameter);
    public void delete(Parameter parameter);
    public List<Parameter> findAllByEtlId(Long etlId);
    public Map<String, String> findAllByEtlIdAsMap(Long etlId);
    public Parameter findOneByIdAndEtlId(Long id, Long etlId);
    public String decodeValueByTypology(Parameter parameter);

    public Parameter findOneById(Long etlId);
}
