package es.gobcan.istac.coetl.platform.common.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.xebialabs.overthere.OverthereConnection;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;

public interface GitService {
    
    public String cloneRepository(Etl etl);
    
    public String replaceRepository(Etl etl, String oldPath);
    
    public void updateRepository(Etl etl);
    
    public void deleteRepository(Etl etl);
    
    public void deleteRepository(String code, TipoPlataformaEjecucion platform);
    
    public String getMainFileContent(Etl etl) throws UnsupportedEncodingException;;
    
    public String getMainFileName(Etl etl);

    Map<String, List<String>> getEtlMetadataInfo(Etl etl) throws UnsupportedEncodingException;

    void checkFileParameters(Etl etl, String originalPath, String newPath, OverthereConnection sudoDestinationConnection);

    void checkFileParameters(Etl etl, String originalPath, String newPath);

}
