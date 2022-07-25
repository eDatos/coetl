package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

import es.gobcan.istac.coetl.domain.Parameter.Type;
import es.gobcan.istac.coetl.domain.Parameter.Typology;

public class ParameterDTO extends AbstractVersionedDTO implements Serializable {

    private static final long serialVersionUID = 9182448019662945127L;

    private Long id;
    private String key;
    private String value;
    private Type type;
    private Typology typology;
    private Long etlId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Typology getTypology() {
        return typology;
    }

    public void setTypology(Typology typology) {
        this.typology = typology;
    }

    public Long getEtlId() {
        return etlId;
    }

    public void setEtlId(Long etlId) {
        this.etlId = etlId;
    }
}
