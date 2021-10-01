package es.gobcan.istac.coetl.pentaho.service;

import es.gobcan.istac.coetl.domain.Etl;

public interface PentahoGitService {
    
    public String cloneRepository(Etl etl);
    
    public String replaceRepository(Etl etl);
    
    public boolean updateRepository(Etl etl);
    
    public String getMainFileContent(Etl etl);
    
    public String getMainFileName(Etl etl);
}
