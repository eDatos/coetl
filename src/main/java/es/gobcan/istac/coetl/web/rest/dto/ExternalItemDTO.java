package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;

public class ExternalItemDTO extends AbstractVersionedAndAuditingWithDeletionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String urn;
    private String urnProvider;
    private String uri;
    private String managementAppUrl;
    private TypeExternalArtefactsEnum type;

    public ExternalItemDTO() {
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

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeExternalArtefactsEnum getType() {
        return type;
    }

    public String getUrnProvider() {
        return urnProvider;
    }

    public void setUrnProvider(String urnProvider) {
        this.urnProvider = urnProvider;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getManagementAppUrl() {
        return managementAppUrl;
    }

    public void setManagementAppUrl(String managementAppUrl) {
        this.managementAppUrl = managementAppUrl;
    }

    public void setType(TypeExternalArtefactsEnum type) {
        this.type = type;
    }
}
