package es.tenerife.secretaria.libro.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.tenerife.secretaria.libro.optimistic.AbstractVersionedEntity;

/**
 * A Operacion.
 */
@Entity
@Table(name = "operacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operacion extends AbstractVersionedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operacion_id_seq")
	@SequenceGenerator(name = "operacion_id_seq", sequenceName = "operacion_id_seq", allocationSize = 50, initialValue = 10)
	private Long id;

	@NotNull
	@Column(name = "accion", nullable = false, updatable = false)
	private String accion;

	@NotNull
	@Column(name = "sujeto", nullable = false, updatable = false)
	private String sujeto;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "roles_operaciones", joinColumns = {
			@JoinColumn(name = "operacion_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "rol_id", referencedColumnName = "id") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Rol> roles = new HashSet<>();

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

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> authorities) {
		this.roles = authorities;
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
		return "Operacion{" + "id=" + getId() + ", accion='" + getAccion() + "'" + ", sujeto='" + getSujeto() + "'"
				+ "}";
	}
}
