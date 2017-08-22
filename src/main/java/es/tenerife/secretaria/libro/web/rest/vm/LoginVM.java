package es.tenerife.secretaria.libro.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import es.tenerife.secretaria.libro.config.Constants;

/**
 * View Model object for storing a user's credentials.
 */

// TODO SECRETARIA-35 Eliminar clase si no se usa
public class LoginVM {

	@Pattern(regexp = Constants.LOGIN_REGEX)
	@NotNull
	@Size(min = 1, max = 50)
	private String username;

	private Boolean rememberMe;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public String toString() {
		return "LoginVM{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
	}
}
