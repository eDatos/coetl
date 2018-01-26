package com.arte.application.template.web.rest.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Pelicula entity.
 */
public class PeliculaDTO extends AbstractVersionedAndAuditingEntityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    private ZonedDateTime fechaEstreno;

    private IdiomaDTO idioma;

    @NotNull
    private List<ActorDTO> actores = new ArrayList<>();

    @NotNull
    private List<CategoriaDTO> categorias = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTime getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(ZonedDateTime fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public IdiomaDTO getIdioma() {
        return idioma;
    }

    public void setIdioma(IdiomaDTO idioma) {
        this.idioma = idioma;
    }

    public List<ActorDTO> getActores() {
        return actores;
    }

    public void setActores(List<ActorDTO> actores) {
        this.actores = actores;
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PeliculaDTO peliculaDTO = (PeliculaDTO) o;
        if (peliculaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), peliculaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PeliculaDTO{" + "id=" + getId() + ", titulo='" + getTitulo() + "'" + ", descripcion='" + getDescripcion() + "'" + ", fechaEstreno='" + getFechaEstreno() + "'" + "}";
    }
}
