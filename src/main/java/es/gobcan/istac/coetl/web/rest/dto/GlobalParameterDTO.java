package es.gobcan.istac.coetl.web.rest.dto;

import es.gobcan.istac.coetl.domain.GlobalParameter.Typology;

import java.io.Serializable;

public class GlobalParameterDTO extends AbstractVersionedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String key;
    private String value;
    private Typology typology;


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

    public Typology getTypology() {
        return typology;
    }

    public void setTypology(Typology typology) {
        this.typology = typology;
    }

}
