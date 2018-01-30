package com.arte.application.template.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actor_id_seq")
    @SequenceGenerator(name = "actor_id_seq", sequenceName = "actor_id_seq", allocationSize = 50, initialValue = 10)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido_1", nullable = false)
    private String apellido1;

    @Column(name = "apellido_2", nullable = false)
    private String apellido2;

    @ManyToMany(mappedBy = "actores", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Pelicula> peliculas = new HashSet<>();

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

    public void addPelicula(Pelicula pelicula) {
        peliculas.add(pelicula);
        pelicula.AddActor(this);
    }

    public void removePelicula(Pelicula pelicula) {
        peliculas.remove(pelicula);
        Set<Actor> set = new HashSet<>(pelicula.getActores());
        set.remove(this);
        pelicula.setAllActores(set);
    }

    public void setPeliculas(Set<Pelicula> peliculas) {
        this.peliculas.clear();
        this.peliculas = new HashSet<>(peliculas);
    }

    public void removeAllPeliculas() {
        for (Pelicula pelicula : new HashSet<>(peliculas)) {
            removePelicula(pelicula);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Actor actor = (Actor) o;
        if (actor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Actor{" + "id=" + getId() + ", nombre='" + getNombre() + "'" + ", apellido1='" + getApellido1() + "'" + ", apellido2='" + getApellido2() + "'" + "}";
    }
}
