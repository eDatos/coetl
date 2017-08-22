package es.tenerife.secretaria.libro.web.rest.dto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import es.tenerife.secretaria.libro.config.Constants;
import es.tenerife.secretaria.libro.domain.Authority;
import es.tenerife.secretaria.libro.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

	private Long id;

	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(max = 256)
	private String imageUrl;

	private boolean activated = false;

	@Size(min = 2, max = 5)
	private String langKey;

	private String createdBy;

	private Instant createdDate;

	private String lastModifiedBy;

	private Instant lastModifiedDate;

	private Set<String> authorities;

	public UserDTO() {
		// Empty constructor needed for Jackson.
	}

	public UserDTO(User user) {
		this.setId(user.getId());
		this.setLogin(user.getLogin());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setEmail(user.getEmail());
		this.setActivated(user.getActivated());
		this.setImageUrl(user.getImageUrl());
		this.setLangKey(user.getLangKey());
		this.setCreatedBy(user.getCreatedBy());
		this.setCreatedDate(user.getCreatedDate());
		this.setLastModifiedBy(user.getLastModifiedBy());
		this.setLastModifiedDate(user.getLastModifiedDate());
		this.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
	}

	public void updateFrom(UserDTO source) {
		this.id = source.getId();
		this.login = source.getLogin();
		this.firstName = source.getFirstName();
		this.lastName = source.getLastName();
		this.email = source.getEmail();
		this.activated = source.isActivated();
		this.imageUrl = source.getImageUrl();
		this.langKey = source.getLangKey();
		this.createdBy = source.getCreatedBy();
		this.createdDate = source.getCreatedDate();
		this.lastModifiedBy = source.getLastModifiedBy();
		this.lastModifiedDate = source.getLastModifiedDate();
		this.authorities = source.getAuthorities();
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

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isActivated() {
		return activated;
	}

	public String getLangKey() {
		return langKey;
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

	public Set<String> getAuthorities() {
		return authorities;
	}

	protected void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
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

	protected void setLangKey(String langKey) {
		this.langKey = langKey;

	}

	protected void setActivated(boolean activated) {
		this.activated = activated;

	}

	protected void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;

	}

	protected void setEmail(String email) {
		this.email = email;
	}

	protected void setLastName(String lastName) {
		this.lastName = lastName;
	}

	protected void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
				+ '\'' + ", email='" + email + '\'' + ", imageUrl='" + imageUrl + '\'' + ", activated=" + activated
				+ ", langKey='" + langKey + '\'' + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy='" + lastModifiedBy + '\'' + ", lastModifiedDate=" + lastModifiedDate
				+ ", authorities=" + authorities + "}";
	}

	public static class Builder {
		private Long id;
		private String login;
		private String firstName;
		private String lastName;
		private String email;
		private String imageUrl;
		private boolean activated = false;
		private String langKey;
		private String createdBy;
		private Instant createdDate;
		private String lastModifiedBy;
		private Instant lastModifiedDate;
		private Set<String> authorities;

		public UserDTO build() {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(this.id);
			userDTO.setLogin(this.login);
			userDTO.setFirstName(this.firstName);
			userDTO.setLastName(this.lastName);
			userDTO.setEmail(this.email);
			userDTO.setImageUrl(this.imageUrl);
			userDTO.setActivated(this.activated);
			userDTO.setLangKey(this.langKey);
			userDTO.setCreatedBy(this.createdBy);
			userDTO.setCreatedDate(this.createdDate);
			userDTO.setLastModifiedBy(this.lastModifiedBy);
			userDTO.setLastModifiedDate(this.lastModifiedDate);
			userDTO.setAuthorities(this.authorities);
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
