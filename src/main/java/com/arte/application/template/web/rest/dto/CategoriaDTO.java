package com.arte.application.template.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Categoria entity.
 */
public class CategoriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoriaDTO categoriaDTO = (CategoriaDTO) o;
        if (categoriaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categoriaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" + "id=" + getId() + ", nombre='" + getNombre() + "'" + "}";
    }
}
