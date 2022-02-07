package es.gobcan.istac.coetl.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_parameters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parameter extends AbstractVersionedEntity implements Serializable {

    private static final long serialVersionUID = 812078062087474781L;

    public enum Type {
        AUTO, MANUAL
    }

    public enum Typology {
        GENERIC, PASSWORD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parameter_id_seq")
    @SequenceGenerator(name = "parameter_id_seq", sequenceName = "parameter_id_seq", initialValue = 10)
    private Long id;

    @NotBlank
    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @NotBlank
    @Column(name = "value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull
    @Column(name = "typology", nullable = false)
    @Enumerated(EnumType.STRING)
    private Typology typology;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etl_fk")
    private Etl etl;

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

    public Etl getEtl() {
        return etl;
    }

    public void setEtl(Etl etl) {
        this.etl = etl;
    }
}
