package es.tenerife.secretaria.libro.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.tenerife.secretaria.libro.config.Constants;
import es.tenerife.secretaria.libro.optimistic.AbstractVersionedAndAuditingEntity;

/**
 * A user.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Usuario extends AbstractVersionedAndAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
	@SequenceGenerator(name = "usuario_id_seq", sequenceName = "usuario_id_seq", allocationSize = 50, initialValue = 10)
	private Long id;

	@NotNull
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@JsonIgnore
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60)
	private String password;

	@Size(max = 50)
	@Column(name = "nombre", length = 50)
	private String nombre;

	@Size(max = 50)
	@Column(name = "apellido1", length = 50)
	private String apellido1;

	@Size(max = 50)
	@Column(name = "apellido2", length = 50)
	private String apellido2;

	@Email
	@Size(min = 5, max = 100)
	@Column(length = 100, unique = true)
	private String email;

	@NotNull
	@Column(nullable = false)
	private boolean activado = false;

	@Size(max = 256)
	@Column(name = "url_imagen", length = 256)
	private String urlImagen;

	@Size(max = 20)
	@Column(name = "clave_reinicio", length = 20)
	@JsonIgnore
	private String claveReinicio;

	@Column(name = "fecha_reinicio")
	private Instant fechaReinicio = null;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "usuario_rol", joinColumns = {
			@JoinColumn(name = "usuario_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "rol_id", referencedColumnName = "id") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Rol> roles = new HashSet<>();

	@Column(name = "deletion_date")
	private ZonedDateTime deletionDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = StringUtils.lowerCase(login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String firstName) {
		this.nombre = firstName;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String lastName) {
		this.apellido1 = lastName;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String lastName) {
		this.apellido2 = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void seturlImagen(String imageUrl) {
		this.urlImagen = imageUrl;
	}

	public boolean getActivado() {
		return activado;
	}

	public void setActivado(boolean activated) {
		this.activado = activated;
	}

	public String getClaveReinicio() {
		return claveReinicio;
	}

	public void setClaveReinicio(String resetKey) {
		this.claveReinicio = resetKey;
	}

	public Instant getFechaReinicio() {
		return fechaReinicio;
	}

	public void setFechaReinicio(Instant resetDate) {
		this.fechaReinicio = resetDate;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public ZonedDateTime getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(ZonedDateTime deletionDate) {
		this.deletionDate = deletionDate;
		if (deletionDate != null) {
			setActivado(false);
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

		Usuario user = (Usuario) o;
		return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Usuario{" + "login='" + login + '\'' + ", nombre='" + nombre + '\'' + ", apellido1='" + apellido1 + '\''
				+ ", email='" + email + '\'' + ", urlImagen='" + urlImagen + '\'' + ", activado='" + activado + '\''
				+ "}";
	}
}
