package es.gobcan.istac.coetl.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Matchers;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import es.gobcan.istac.coetl.domain.EnabledTokenEntity;
import es.gobcan.istac.coetl.repository.EnabledTokenRepository;

import es.gobcan.istac.coetl.security.util.SecurityCookiesUtil;
import es.gobcan.istac.coetl.service.impl.EnabledTokenServiceImpl;
import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenProviderTest {

    private final static String ANONYMOUS = "ROLE_ANONYMOUS";
    private final static String SERVICE_TICKET = "ST-13-bGFVKRi9AwprEZutH8HdRgBvQh0estadisticas";
    private final String secretKey = "e5c9ee274ae87bc031adda32e27fa98b9290da83";
    private final long ONE_MINUTE = 60000;
    private JHipsterProperties jHipsterProperties;
    private TokenProvider tokenProvider;
    private EnabledTokenRepository enabledTokenRepository;

    @Before
    public void setup() {
        jHipsterProperties = Mockito.mock(JHipsterProperties.class);
        enabledTokenRepository = Mockito.mock(EnabledTokenRepository.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(SecurityCookiesUtil.SERVICE_TICKET_COOKIE, SERVICE_TICKET);
        tokenProvider = new TokenProvider(jHipsterProperties, new EnabledTokenServiceImpl(enabledTokenRepository), request);

        ReflectionTestUtils.setField(tokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", ONE_MINUTE);
    }

    @Test
    public void testReturnFalseWhenJWThasInvalidSignature() {
        boolean isTokenValid = tokenProvider.validateToken(createTokenWithDifferentSignature());

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisMalformed() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        String invalidToken = token.substring(1);
        boolean isTokenValid = tokenProvider.validateToken(invalidToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisExpired() {
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", -ONE_MINUTE);

        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        boolean isTokenValid = tokenProvider.validateToken(token);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisUnsupported() {
        String unsupportedToken = createUnsupportedToken();

        boolean isTokenValid = tokenProvider.validateToken(unsupportedToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisInvalid() {
        boolean isTokenValid = tokenProvider.validateToken("");

        assertThat(isTokenValid).isEqualTo(false);
    }
    
    @Test
    public void testReturnFalseWhenJWTisNotInTheDatabase() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        Mockito.when(enabledTokenRepository.existsByToken(token)).thenReturn(false);
        boolean isTokenValid = tokenProvider.validateToken(token);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testJwtIsBeingSavedInTheDatabase() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        Mockito.verify(enabledTokenRepository, Mockito.times(1)).save(new EnabledTokenEntity(token, SERVICE_TICKET, Matchers.any()));
    }

    private Authentication createAuthentication() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(TokenProviderTest.ANONYMOUS));
        return new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities);
    }

    private String createUnsupportedToken() {
        return Jwts.builder().setPayload("payload").signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    private String createTokenWithDifferentSignature() {
        return Jwts.builder().setSubject("anonymous").signWith(SignatureAlgorithm.HS512, "e5c9ee274ae87bc031adda32e27fa98b9290da90").setExpiration(new Date(new Date().getTime() + ONE_MINUTE))
                .compact();
    }
}
