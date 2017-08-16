package es.tenerife.secretaria.libro.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private final Cas cas = new Cas();

	public Cas getCas() {
		return cas;
	}

	public static class Cas {

		// Required
		private String endpoint;
		private String applicationHome;

		// Optional
		private String login;
		private String logout;
		private String validate;

		public String getEndpoint() {
			return endpoint;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}

		public String getLogin() {
			if (StringUtils.isEmpty(login)) {
				return StringUtils.removeEnd(endpoint, "/") + "/login";
			}
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getLogout() {
			if (StringUtils.isEmpty(logout)) {
				return StringUtils.removeEnd(endpoint, "/") + "/logout";
			}
			return logout;
		}

		public void setLogout(String logout) {
			this.logout = logout;
		}

		public String getValidate() {
			if (StringUtils.isEmpty(logout)) {
				return endpoint;
			}
			return validate;
		}

		public void setValidate(String validate) {
			this.validate = validate;
		}

		public String getApplicationHome() {
			return applicationHome;
		}

		public void setApplicationHome(String applicationHome) {
			this.applicationHome = applicationHome;
		}
	}

}
