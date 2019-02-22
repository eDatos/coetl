package es.gobcan.istac.coetl.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.google.common.primitives.Ints;

import es.gobcan.istac.coetl.config.Constants;

public class JWTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String TOKEN = "token";
    public static final String JHI_AUTHENTICATIONTOKEN = "jhi-authenticationtoken";

    private TokenProvider tokenProvider;

    private final Environment env;

    public JWTAuthenticationSuccessHandler(TokenProvider tokenProvider, Environment env) {
        this.tokenProvider = tokenProvider;
        this.env = env;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean rememberMe = false;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        Cookie cookie = new Cookie(JHI_AUTHENTICATIONTOKEN, jwt);
        cookie.setSecure(env.acceptsProfiles(Constants.SPRING_PROFILE_ENV));
        cookie.setMaxAge(Ints.saturatedCast(7200));
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);

        // For evict JSESSIONID, invalidate the session of CASFilter
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        handle(request, response, authentication);
    }
}