package es.tenerife.secretaria.libro.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
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
	@Column(name = "apellidos", length = 50)
	private String apellidos;

	@Email
	@Size(min = 5, max = 100)
	@Column(length = 100, unique = true)
	private String email;

	@NotNull
	@Column(nullable = false)
	private boolean activado = false;

	@Size(min = 2, max = 5)
	@Column(name = "idioma", length = 5)
	private String idioma;

	@Size(max = 256)
	@Column(name = "url_imagen", length = 256)
	private String urlImagen;

	@Size(max = 20)
	@Column(name = "clave_activacion", length = 20)
	@JsonIgnore
	private String claveActivacion;

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
					@JoinColumn(name = "rol_nombre", referencedColumnName = "nombre") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Rol> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	// Lowercase the login before saving it in database
	public void setLogin(String login) {
		this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String lastName) {
		this.apellidos = lastName;
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

	public String getClaveActivacion() {
		return claveActivacion;
	}

	public void setClaveActivacion(String activationKey) {
		this.claveActivacion = activationKey;
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

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String langKey) {
		this.idioma = langKey;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
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
		return "User{" + "login='" + login + '\'' + ", firstName='" + nombre + '\'' + ", lastName='" + apellidos + '\''
				+ ", email='" + email + '\'' + ", imageUrl='" + urlImagen + '\'' + ", activated='" + activado + '\''
				+ ", langKey='" + idioma + '\'' + ", activationKey='" + claveActivacion + '\'' + "}";
	}
}
