package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

public class ComputationalThreadsDTO extends ComputationalThreadsBaseDTO implements Serializable {

    private static final long serialVersionUID = 8045655622296244049L;

    private String description;

    public ComputationalThreadsDTO() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
