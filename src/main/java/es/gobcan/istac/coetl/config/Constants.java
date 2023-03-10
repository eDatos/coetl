package es.gobcan.istac.coetl.config;

import java.util.Locale;

public final class Constants {

    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String SPRING_PROFILE_ENV = "env";

    public static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("es");

    public static final String DEFAULT_PLATFORM_WATCH_CRON = "0 * * * * *";

    private Constants() {
    }
}
