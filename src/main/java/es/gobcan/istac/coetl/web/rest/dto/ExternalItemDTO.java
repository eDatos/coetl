package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;

import es.gobcan.istac.coetl.domain.Etl.Type;

public class ExternalItemDTO extends AbstractVersionedAndAuditingWithDeletionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private InternationalString name;
    private String urn;


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

    public InternationalString getName() {
        return this.name;
    }

    public void setName(InternationalString name) {
        this.name = name;
    }
}
