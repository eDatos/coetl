package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

public class ComputationalThreadsBaseDTO extends AbstractVersionedAndAuditingWithDeletionDTO implements Serializable {

    private static final long serialVersionUID = 7581847502743845396L;

    private Long id;
    private String code;
    private String name;

    public ComputationalThreadsBaseDTO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
