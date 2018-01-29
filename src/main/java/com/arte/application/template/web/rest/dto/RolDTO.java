package com.arte.application.template.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

public class RolDTO extends AbstractVersionedDTO implements Serializable, Comparable<RolDTO> {

    private static final long serialVersionUID = 3662329403989469628L;

    @JsonView(Views.Minimal.class)
    private Long id;

    @JsonView(Views.Minimal.class)
    private String codigo;

    @JsonView(Views.Minimal.class)
    private String nombre;

    private Set<OperacionDTO> operaciones = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<OperacionDTO> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(Set<OperacionDTO> operaciones) {
        this.operaciones = operaciones;
    }

    @Override
    public int compareTo(RolDTO o) {
        if (o == null || o.getCodigo() == null) {
            return -1;
        }
        if (this.getCodigo() == null) {
            return 1;
        }
        return this.getCodigo().compareTo(o.getCodigo());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
