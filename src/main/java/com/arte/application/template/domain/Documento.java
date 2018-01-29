package com.arte.application.template.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.arte.application.template.optimistic.AbstractVersionedAndAuditingEntity;

@Entity
@Table(name = "documento")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Documento extends AbstractVersionedAndAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_id_seq")
    @SequenceGenerator(name = "documento_id_seq", sequenceName = "documento_id_seq", allocationSize = 50, initialValue = 10)
    private Long id;

    @NotNull
    @Lob
    @Column(name = "data", nullable = false)
    private Blob data;

    @NotNull
    @Column(name = "data_content_type", nullable = false)
    private String dataContentType;

    @Column(name = "name")
    private String name;

    @Column(name = "length")
    private Long length;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getData() {
        return data;
    }

    public void setData(Blob data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Documento localFile = (Documento) o;
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
        return "LocalFile{" + "id=" + id + ", data='" + data + "'" + ", dataContentType='" + dataContentType + "'" + ", name='" + name + "'" + ", length='" + length + "'" + '}';
    }
}
