package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.GlobalParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface GlobalParameterService {


    public GlobalParameter create(GlobalParameter parameter);
    public GlobalParameter update(GlobalParameter parameter);
    public void delete(GlobalParameter parameter);
    public String decodeValueByTypology(GlobalParameter parameter);

    public Page<GlobalParameter> findAll(Pageable pageable);
    public GlobalParameter findOneById(Long etlId);

    public Map<String, String> findAllGlobalParametersAsMap();
}
