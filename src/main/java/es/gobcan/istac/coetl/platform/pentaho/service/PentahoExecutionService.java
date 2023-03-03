package es.gobcan.istac.coetl.platform.pentaho.service;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.WebResultDTO;

public interface PentahoExecutionService {

    Execution execute(Etl etl, Type type);
    WebResultDTO runEtl(Etl etl, final String etlFilename, final String idExecution);
    WebResultDTO removeEtl(Etl etl, final String etlFilename, final String idExecution);
}
