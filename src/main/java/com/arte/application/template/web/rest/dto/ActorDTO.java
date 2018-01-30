package com.arte.application.template.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Actor entity.
 */
public class ActorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido1;

    private String apellido2;

    private Set<PeliculaDTO> peliculas = new HashSet<>();

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

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Set<PeliculaDTO> getPeliculas() {
        return peliculas;
    }

    public void setApellido2(Set<PeliculaDTO> peliculas) {
        this.peliculas = peliculas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActorDTO actorDTO = (ActorDTO) o;
        if (actorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActorDTO{" + "id=" + getId() + ", nombre='" + getNombre() + "'" + ", apellido1='" + getApellido1() + "'" + ", apellido2='" + getApellido2() + "'" + "}";
    }
}
