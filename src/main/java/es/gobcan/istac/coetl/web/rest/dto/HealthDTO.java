package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

public class HealthDTO implements Serializable {

    private static final long serialVersionUID = -970660254313958684L;

    private Long id;
    private String serviceName;
    private String endpoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}