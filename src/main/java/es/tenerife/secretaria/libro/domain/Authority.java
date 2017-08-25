package es.tenerife.secretaria.libro.domain;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.tenerife.secretaria.libro.optimistic.AbstractVersionedEntity;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "jhi_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Authority extends AbstractVersionedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_id_seq")
	@SequenceGenerator(name = "rol_id_seq", sequenceName = "rol_id_seq")
	private Long id;

	@NotNull
	@Size(min = 0, max = 50)
	@Id
	@Column(length = 50)
	private String name;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "roles_operaciones", joinColumns = {
			@JoinColumn(name = "rol_name", referencedColumnName = "name") }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "id") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Operacion> operaciones = new HashSet<>();

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "jhi_user_authority", joinColumns = {
			@JoinColumn(name = "authority_name", referencedColumnName = "name") }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "id") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<User> users = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
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

		Authority authority = (Authority) o;

		return !(name != null ? !name.equals(authority.name) : authority.name != null);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Authority{" + "name='" + name + '\'' + "}";
	}

}
