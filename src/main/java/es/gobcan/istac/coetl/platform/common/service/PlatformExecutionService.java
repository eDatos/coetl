package es.gobcan.istac.coetl.platform.common.service;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Type;


public interface PlatformExecutionService {

    Execution execute(Etl etl, Type type, String executor);
    Object runEtl(Etl etl, final String etlFilename, final String idExecution);
    Object removeEtl(Etl etl, final String etlFilename, final String idExecution);
    Object registerETL(Etl etl);
    void notifyExecutionError(Etl etl);
}
