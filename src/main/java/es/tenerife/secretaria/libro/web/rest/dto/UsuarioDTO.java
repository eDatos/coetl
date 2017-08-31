package es.tenerife.secretaria.libro.web.rest.dto;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import es.tenerife.secretaria.libro.config.Constants;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.Usuario;

/**
 * A DTO representing a user, with his authorities.
 */
public class UsuarioDTO {

	private Long id;

	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@Size(max = 50)
	private String nombre;

	@Size(max = 50)
	private String apellido1;

	@Size(max = 50)
	private String apellido2;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(max = 256)
	private String urlImage;

	private boolean activado = false;

	@Size(min = 2, max = 5)
	private String idioma;

	private String createdBy;

	private Instant createdDate;

	private String lastModifiedBy;

	private Instant lastModifiedDate;

	private Set<String> roles;

	private ZonedDateTime deletionDate;

	public UsuarioDTO() {
		// Empty constructor needed for Jackson.
	}

	public UsuarioDTO(Usuario user) {
		this.setId(user.getId());
		this.setLogin(user.getLogin());
		this.setNombre(user.getNombre());
		this.setApellido1(user.getApellido1());
		this.setApellido2(user.getApellido2());
		this.setEmail(user.getEmail());
		this.setActivo(user.getActivado());
		this.setUrlImagen(user.getUrlImagen());
		this.setIdioma(user.getIdioma());
		this.setCreatedBy(user.getCreatedBy());
		this.setCreatedDate(user.getCreatedDate());
		this.setLastModifiedBy(user.getLastModifiedBy());
		this.setLastModifiedDate(user.getLastModifiedDate());
		this.setRoles(user.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet()));
		this.setDeletionDate(user.getDeletionDate());
	}

	public void updateFrom(UsuarioDTO source) {
		this.id = source.getId();
		this.login = source.getLogin();
		this.nombre = source.getNombre();
		this.apellido1 = source.getApellido1();
		this.apellido2 = source.getApellido2();
		this.email = source.getEmail();
		this.activado = source.isActivo();
		this.urlImage = source.getUrlImagen();
		this.idioma = source.getIdioma();
		this.createdBy = source.getCreatedBy();
		this.createdDate = source.getCreatedDate();
		this.lastModifiedBy = source.getLastModifiedBy();
		this.lastModifiedDate = source.getLastModifiedDate();
		this.roles = source.getRoles();
	}

	public static Builder builder() {
		return new Builder();
	}

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
		this.login = login;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public String getEmail() {
		return email;
	}

	public String getUrlImagen() {
		return urlImage;
	}

	public boolean isActivo() {
		return activado;
	}

	public String getIdioma() {
		return idioma;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Set<String> getRoles() {
		return roles;
	}

	protected void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	protected void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;

	}

	protected void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;

	}

	protected void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;

	}

	protected void setIdioma(String langKey) {
		this.idioma = langKey;

	}

	protected void setActivo(boolean activated) {
		this.activado = activated;

	}

	protected void setUrlImagen(String imageUrl) {
		this.urlImage = imageUrl;

	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setApellido1(String lastName) {
		this.apellido1 = lastName;
	}

	public void setApellido2(String lastName) {
		this.apellido2 = lastName;
	}

	public void setNombre(String firstName) {
		this.nombre = firstName;
	}

	public ZonedDateTime getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(ZonedDateTime deletionDate) {
		this.deletionDate = deletionDate;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + nombre + '\'' + ", lastName='" + apellido1
				+ '\'' + ", email='" + email + '\'' + ", imageUrl='" + urlImage + '\'' + ", activated=" + activado
				+ ", langKey='" + idioma + '\'' + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy='" + lastModifiedBy + '\'' + ", lastModifiedDate=" + lastModifiedDate
				+ ", authorities=" + roles + "}";
	}

	public static class Builder {
		private Long id;
		private String login;
		private String firstName;
		private String lastName;
		private String lastName2;
		private String email;
		private String imageUrl;
		private boolean activated = false;
		private String langKey;
		private String createdBy;
		private Instant createdDate;
		private String lastModifiedBy;
		private Instant lastModifiedDate;
		private Set<String> authorities;

		public UsuarioDTO build() {
			UsuarioDTO userDTO = new UsuarioDTO();
			userDTO.setId(this.id);
			userDTO.setLogin(this.login);
			userDTO.setNombre(this.firstName);
			userDTO.setApellido1(this.lastName);
			userDTO.setApellido2(this.lastName2);
			userDTO.setEmail(this.email);
			userDTO.setUrlImagen(this.imageUrl);
			userDTO.setActivo(this.activated);
			userDTO.setIdioma(this.langKey);
			userDTO.setCreatedBy(this.createdBy);
			userDTO.setCreatedDate(this.createdDate);
			userDTO.setLastModifiedBy(this.lastModifiedBy);
			userDTO.setLastModifiedDate(this.lastModifiedDate);
			userDTO.setRoles(this.authorities);
			return userDTO;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setLogin(String login) {
			this.login = login;
			return this;
		}

		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder setLastName2(String lastName) {
			this.lastName2 = lastName;
			return this;
		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public Builder setActivated(boolean activated) {
			this.activated = activated;
			return this;
		}

		public Builder setLangKey(String langKey) {
			this.langKey = langKey;
			return this;
		}

		public Builder setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public Builder setCreatedDate(Instant createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public Builder setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public Builder setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
			return this;
		}

		public Builder setAuthorities(Set<String> authorities) {
			this.authorities = authorities;
			return this;
		}

	}
}
