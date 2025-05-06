package es.gobcan.istac.coetl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tb_global_parameters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GlobalParameter extends AbstractVersionedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Typology {
        GENERIC, PASSWORD, FILE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_parameter_id_seq")
    @SequenceGenerator(name = "global_parameter_id_seq", sequenceName = "global_parameter_id_seq", initialValue = 10)
    private Long id;

    @NotBlank
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @NotBlank
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "typology", nullable = false)
    @Enumerated(EnumType.STRING)
    private Typology typology;

    @Override
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
