package es.gobcan.coetl.pentaho.service;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Type;

public interface PentahoExecutionService {

    Execution execute(Etl etl, Type type);
}
