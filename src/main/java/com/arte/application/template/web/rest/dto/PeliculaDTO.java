package com.arte.application.template.web.rest.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Pelicula entity.
 */
public class PeliculaDTO extends AbstractVersionedAndAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    private ZonedDateTime fechaEstreno;

    private IdiomaDTO idioma;
    
    private Double presupuesto;

    @NotNull
    private Set<ActorDTO> actores = new HashSet<>();

    @NotNull
    private Set<CategoriaDTO> categorias = new HashSet<>();

    private DocumentoDTO documento;
    
    private Set<String> premios = new HashSet<>();

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
    
    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Set<ActorDTO> getActores() {
        return actores;
    }

    public void setActores(Set<ActorDTO> actores) {
        this.actores = actores;
    }

    public Set<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public DocumentoDTO getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoDTO documento) {
        this.documento = documento;
    }

    public Set<String> getPremios() {
        return premios;
    }

    public void setPremios(Set<String> premios) {
        this.premios = premios;
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
