package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Parameter;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ParameterService {

    public List<Parameter> createDefaultParameters(Etl etl, Map<String, String> parameters);
    public void deleteDefaultParameters(Etl etl);
    public Parameter create(Parameter parameter);
    public Long storeFile(MultipartFile file, Long idEtl);
    public void updateFile(MultipartFile file, Parameter originalParameter, Parameter currentParameter, Long idEtl);
    public void deleteFile(Parameter parameter, Long idEtl);
    public Parameter update(Parameter parameter);
    public void delete(Parameter parameter);
    public List<Parameter> findAllByEtlId(Long etlId);
    public Map<String, String> findAllByEtlIdAsMap(Long etlId);
    public Parameter findOneByIdAndEtlId(Long id, Long etlId);
    public Parameter findOneByKeyAndEtlId(String key, Long etlId);
    public Parameter copyParameter(Parameter originalParameter);
    public Parameter getOneById(Long paramId);
    public String decodeValueByTypology(Parameter parameter);
    public Parameter findOneById(Long etlId);
}
