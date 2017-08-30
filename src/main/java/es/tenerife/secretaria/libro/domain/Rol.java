package es.tenerife.secretaria.libro.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.tenerife.secretaria.libro.optimistic.AbstractVersionedEntity;
import es.tenerife.secretaria.libro.optimistic.OptLockId;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "rol")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rol extends AbstractVersionedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 0, max = 50)
	@Id
	@Column(length = 50)
	@OptLockId
	private String nombre;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "roles_operaciones", joinColumns = {
			@JoinColumn(name = "rol_nombre", referencedColumnName = "nombre") }, inverseJoinColumns = {
					@JoinColumn(name = "operacion_id", referencedColumnName = "id") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Operacion> operaciones = new HashSet<>();

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "usuario_rol", joinColumns = {
			@JoinColumn(name = "rol_nombre", referencedColumnName = "nombre") }, inverseJoinColumns = {
					@JoinColumn(name = "usuario_id", referencedColumnName = "id") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Usuario> usuarios = new HashSet<>();

	public String getId() {
		return nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> users) {
		this.usuarios = users;
	}

	public Set<Operacion> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(Set<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Rol authority = (Rol) o;

		return !(nombre != null ? !nombre.equals(authority.nombre) : authority.nombre != null);
	}

	@Override
	public int hashCode() {
		return nombre != null ? nombre.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Authority{" + "name='" + nombre + '\'' + "}";
	}

}
