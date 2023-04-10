package es.gobcan.istac.coetl.platform.pentaho.web.rest.dto;

import es.gobcan.istac.coetl.platform.pentaho.enumeration.Status;

public interface EtlStatusDTO {

    public String getName();

    public void setName(String name);

    public String getId();

    public void setId(String id);

    public Status getStatus();

    public void setStatus(Status status);

    public String getErrorDescription();

    public void setErrorDescription(String errorDescription);

    public boolean isRunning();

    public boolean isWaiting();

    public boolean isFinished();

    public boolean isFinishedWithErrors();

    public boolean isStoppedWithErrors();

    public boolean isStopped();

}
