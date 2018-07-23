package es.gobcan.coetl.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import es.gobcan.coetl.optimistic.AbstractVersionedAndAuditingEntity;

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
    @Column(name = "fecha_estreno", nullable = false)
    private ZonedDateTime fechaEstreno;

    @ManyToOne(optional = true)
    @JoinColumn(name = "idioma_id", nullable = true)
    private Idioma idioma;

    @Column(name = "presupuesto")
    private Double presupuesto;

    @NotNull
    @ManyToMany
    @JoinTable(name = "pelicula_actor", joinColumns = @JoinColumn(name = "pelicula_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    @Valid
    private Set<Actor> actores = new HashSet<>();

    @NotNull
    @ManyToMany
    @JoinTable(name = "pelicula_categoria", joinColumns = @JoinColumn(name = "pelicula_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "categoria_id", referencedColumnName = "id"))
    @Valid
    private Set<Categoria> categorias = new HashSet<>();

    @OneToOne(optional = true, orphanRemoval = true)
    @JoinColumn(name = "documento_id", nullable = true)
    private Documento documento;

    @ElementCollection
    @JoinTable(name = "pelicula_premio", joinColumns = @JoinColumn(name = "pelicula_id", referencedColumnName = "id"))
    @Column(name = "premio", nullable = false)
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

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public void setAllActores(Set<Actor> actores) {
        removeAllActores();
        for (Actor actor : actores) {
            addActor(actor);
        }
    }

    public void addActor(Actor actor) {
        this.actores.add(actor);
    }

    public void removeAllActores() {
        this.actores.clear();
    }

    public Set<Actor> getActores() {
        return Collections.unmodifiableSet(this.actores);
    }

    public void setAllCategorias(Set<Categoria> categorias) {
        removeAllCategorias();
        for (Categoria categoria : categorias) {
            addCategoria(categoria);
        }
    }

    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }

    public void removeAllCategorias() {
        this.categorias.clear();
    }

    public Set<Categoria> getCategorias() {
        return this.categorias;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
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
        return "Pelicula (id = " + getId() + ", Titulo = " + getTitulo() + ")";
    }
}
