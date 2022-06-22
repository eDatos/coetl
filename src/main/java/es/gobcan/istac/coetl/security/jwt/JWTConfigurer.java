package es.gobcan.istac.coetl.security.jwt;

import es.gobcan.istac.coetl.config.ApplicationProperties;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    private ApplicationProperties applicationProperties;

    public JWTConfigurer(TokenProvider tokenProvider,  ApplicationProperties applicationProperties) {
        this.tokenProvider = tokenProvider;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JWTFilter customFilter = new JWTFilter(tokenProvider, applicationProperties);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
