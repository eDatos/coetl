package com.arte.application.template.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.arte.application.template.domain.Actor.Genero;

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

    private Genero genero;

    private boolean oscarizado;
    
    private Set<DocumentoDTO> documentos;

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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public boolean isOscarizado() {
        return oscarizado;
    }

    public void setOscarizado(boolean oscarizado) {
        this.oscarizado = oscarizado;
    }

    public Set<DocumentoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<DocumentoDTO> documentos) {
        this.documentos = documentos;
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
        return "ActorDTO (id = " + getId() + ", Nombre = " + getNombre() + ", Apellido1 = " + getApellido1() + ", Apellido2 = " + getApellido2() + ")";
    }
}
