package es.gobcan.coetl.service;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution.Type;

public interface PentahoExecutionService {

    void execute(Etl etl, Type type);
}
