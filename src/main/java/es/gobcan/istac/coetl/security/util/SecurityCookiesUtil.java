package es.gobcan.istac.coetl.security.util;

import javax.servlet.http.Cookie;
import java.util.Arrays;

public class SecurityCookiesUtil {

    public static final String SERVICE_TICKET_COOKIE = "service_ticket";
    public static final String ROOT_PATH = "/";

    public static String getCookiePath() {
        return ROOT_PATH;
    }

    public static String getServiceTicketCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
            .filter(c -> SERVICE_TICKET_COOKIE.equals(c.getName()))
            .map(Cookie::getValue)
            .findAny().orElse(null);
    }
}
