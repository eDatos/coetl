package es.gobcan.istac.coetl.platform.common.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import es.gobcan.istac.coetl.domain.Etl;

public interface GitService {
    
    public String cloneRepository(Etl etl);
    
    public String replaceRepository(Etl etl);
    
    public boolean updateRepository(Etl etl);
    
    public String getMainFileContent(Etl etl) throws UnsupportedEncodingException;;
    
    public String getMainFileName(Etl etl);

    Map<String, List<String>> getEtlMetadataInfo(Etl etl) throws UnsupportedEncodingException;
}
