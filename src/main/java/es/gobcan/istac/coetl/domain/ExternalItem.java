package es.gobcan.istac.coetl.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.siemac.edatos.core.common.enume.converter.TypeExternalArtefactsEnumConverter;

@Entity
@Table(name = "tb_external_items")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@PrimaryKeyJoinColumn(name = "external_item_fk")
public class ExternalItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "external_item_id_seq")
    @SequenceGenerator(name = "external_item_id_seq", sequenceName = "external_item_id_seq", initialValue = 10)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @NotBlank
    @Column(name = "urn",length = 4000, nullable = false)
    private String urn;

    @NotNull
    @Column(name = "type", nullable = false)
    @Convert(converter = TypeExternalArtefactsEnumConverter.class)
    private TypeExternalArtefactsEnum type;

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

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public TypeExternalArtefactsEnum getType() {
        return type;
    }

    public void setType(TypeExternalArtefactsEnum type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalItem localFile = (ExternalItem) o;
        if (localFile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, localFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExternalItem (id = " + getId() + ", name = " + getName() + ")";
    }
}
