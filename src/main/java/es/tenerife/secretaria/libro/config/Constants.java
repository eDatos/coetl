package es.tenerife.secretaria.libro.config;

import java.util.Locale;

public final class Constants {

	public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

	public static final String SYSTEM_ACCOUNT = "system";
	public static final String ANONYMOUS_USER = "anonymoususer";

	public static final String SPRING_PROFILE_ENV = "env";

	public static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("es");

	private Constants() {
	}
}
