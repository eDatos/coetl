package es.gobcan.istac.coetl.pentaho.web.rest.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.gobcan.istac.coetl.pentaho.enumeration.Status;

@XmlRootElement(name = "transstatus")
public class TransStatusDTO implements PentahoResponseDTO, EtlStatusDTO {

    private static final long serialVersionUID = 1L;

    private String name;
    private String id;
    private Status status;
    private String errorDescription;

    @XmlElement(name = "transname")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "status_desc")
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @XmlElement(name = "error_desc")
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public boolean isRunning() {
        return Status.RUNNING.equals(status);
    }

    public boolean isWaiting() {
        return Status.WAITING.equals(status);
    }

    public boolean isFinished() {
        return Status.FINISHED.equals(status) || isFinishedWithErrors();
    }

    public boolean isFinishedWithErrors() {
        return Status.FINISHED_WITH_ERRORS.equals(status);
    }
}
