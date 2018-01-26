package com.arte.application.template.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.arte.application.template.optimistic.AbstractVersionedAndAuditingEntity;

/**
 * A Pelicula.
 */
@Entity
@Table(name = "pelicula")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pelicula extends AbstractVersionedAndAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pelicula_id_seq")
    @SequenceGenerator(name = "pelicula_id_seq", sequenceName = "pelicula_id_seq", allocationSize = 50, initialValue = 10)
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "annoestreno", nullable = false)
    private ZonedDateTime annoEstreno;

    @ManyToOne(optional = true)
    @JoinColumn(name = "idioma_id", nullable = true)
    private Idioma idioma;

    @NotNull
    @ManyToMany
    @JoinTable(name = "pelicula_actor", joinColumns = @JoinColumn(name = "pelicula_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    @Valid
    private List<Actor> actores = new ArrayList<>();

    @NotNull
    @ManyToMany
    @JoinTable(name = "pelicula_categoria", joinColumns = @JoinColumn(name = "pelicula_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "categoria_id", referencedColumnName = "id"))
    @Valid
    private List<Categoria> categorias = new ArrayList<>();

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

    public ZonedDateTime getAnnoEstreno() {
        return annoEstreno;
    }

    public void setAnnoEstreno(ZonedDateTime annoestreno) {
        this.annoEstreno = annoestreno;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public void setAllActores(List<Actor> actores) {
        removeAllActores();
        for (Actor actor : actores) {
            AddActor(actor);
        }
    }

    public void AddActor(Actor actor) {
        this.actores.add(actor);
    }

    public void removeAllActores() {
        this.actores.clear();
    }

    public List<Actor> getActores() {
        return Collections.unmodifiableList(this.actores);
    }

    public void setAllCategorias(List<Categoria> categorias) {
        removeAllActores();
        for (Categoria categoria : categorias) {
            AddCategoria(categoria);
        }
    }

    public void AddCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }

    public void removeAllCategorias() {
        this.categorias.clear();
    }

    public List<Categoria> getCategorias() {
        return Collections.unmodifiableList(this.categorias);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pelicula pelicula = (Pelicula) o;
        if (pelicula.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pelicula.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pelicula{" + "id=" + getId() + ", titulo='" + getTitulo() + "'" + ", descripcion='" + getDescripcion() + "'" + ", annoestreno='" + getAnnoEstreno() + "'" + "}";
    }
}
