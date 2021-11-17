package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

import es.gobcan.istac.coetl.domain.Etl.Type;

public class EtlBaseDTO extends AbstractVersionedAndAuditingWithDeletionDTO implements Serializable {

    private static final long serialVersionUID = 3862403743786317151L;

    private Long id;
    private String code;
    private String name;
    private String organizationInCharge;
    private Type type;
    private String executionPlanning;
    private ExternalItemDTO externalItem;

    public EtlBaseDTO() {
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

    public String getOrganizationInCharge() {
        return organizationInCharge;
    }

    public void setOrganizationInCharge(String organizationInCharge) {
        this.organizationInCharge = organizationInCharge;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getExecutionPlanning() {
        return executionPlanning;
    }

    public void setExecutionPlanning(String executionPlanning) {
        this.executionPlanning = executionPlanning;
    }

    public ExternalItemDTO getExternalItem() {
        return externalItem;
    }

    public void setExternalItem(ExternalItemDTO externalItem) {
        this.externalItem = externalItem;
    }
}
