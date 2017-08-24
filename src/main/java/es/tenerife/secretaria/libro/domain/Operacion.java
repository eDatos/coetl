package es.tenerife.secretaria.libro.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Operacion.
 */
@Entity
@Table(name = "operacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "accion", nullable = false)
    private String accion;

    @NotNull
    @Column(name = "sujeto", nullable = false)
    private String sujeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccion() {
        return accion;
    }

    public Operacion accion(String accion) {
        this.accion = accion;
        return this;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getSujeto() {
        return sujeto;
    }

    public Operacion sujeto(String sujeto) {
        this.sujeto = sujeto;
        return this;
    }

    public void setSujeto(String sujeto) {
        this.sujeto = sujeto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operacion operacion = (Operacion) o;
        if (operacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Operacion{" +
            "id=" + getId() +
            ", accion='" + getAccion() + "'" +
            ", sujeto='" + getSujeto() + "'" +
            "}";
    }
}
