package es.gobcan.istac.coetl.security.jwt;

import es.gobcan.istac.coetl.config.ApplicationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Filters incoming requests and installs a Spring Security principal if a
 * header corresponding to a valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;

    private ApplicationProperties applicationProperties;

    public JWTFilter(TokenProvider tokenProvider, ApplicationProperties applicationProperties) {
        this.tokenProvider = tokenProvider;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        // Header
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        // Cookie
        if (request.getCookies() != null) {
            Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(JWTAuthenticationSuccessHandler.JHI_AUTHENTICATIONTOKEN)).findFirst();
            if (tokenCookie.isPresent()) {
                return tokenCookie.get().getValue();
            }
        }

        return null;
    }

    private String getAuthenticationTokenByInstance(){
        return  JWTAuthenticationSuccessHandler.JHI_AUTHENTICATIONTOKEN + "_" + applicationProperties.getInstallation().getInstance().toLowerCase();
    }
}
