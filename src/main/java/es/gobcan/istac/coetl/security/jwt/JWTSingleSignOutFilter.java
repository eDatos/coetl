package es.gobcan.istac.coetl.security.jwt;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.util.AbstractConfigurationFilter;

/**
 * Handler made by modifying org.jasig.cas.client.session.SingleSignOutFilter in order to use JWTSingleSignOutHandler instead of org.jasig.cas.client.session.SingleSignOutHandler.
 */
public class JWTSingleSignOutFilter extends AbstractConfigurationFilter {

    private final JWTSingleSignOutHandler handler;

    private final AtomicBoolean handlerInitialized = new AtomicBoolean(false);

    public JWTSingleSignOutFilter(JWTSingleSignOutHandler handler) {
        this.handler = handler;
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        if (!isIgnoreInitConfiguration()) {
            setArtifactParameterName(getString(ConfigurationKeys.ARTIFACT_PARAMETER_NAME));
            setLogoutParameterName(getString(ConfigurationKeys.LOGOUT_PARAMETER_NAME));
            setFrontLogoutParameterName(getString(ConfigurationKeys.FRONT_LOGOUT_PARAMETER_NAME));
            setRelayStateParameterName(getString(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME));
            setCasServerUrlPrefix(getString(ConfigurationKeys.CAS_SERVER_URL_PREFIX));
            handler.setArtifactParameterOverPost(getBoolean(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST));
        }
        handler.init();
        handlerInitialized.set(true);
    }

    public void setArtifactParameterName(final String name) {
        handler.setArtifactParameterName(name);
    }

    public void setLogoutParameterName(final String name) {
        handler.setLogoutParameterName(name);
    }

    public void setFrontLogoutParameterName(final String name) {
        handler.setFrontLogoutParameterName(name);
    }

    public void setRelayStateParameterName(final String name) {
        handler.setRelayStateParameterName(name);
    }

    public void setCasServerUrlPrefix(final String casServerUrlPrefix) {
        handler.setCasServerUrlPrefix(casServerUrlPrefix);
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * <p>Workaround for now for the fact that Spring Security will fail since it doesn't call {@link #init(javax.servlet.FilterConfig)}.</p>
         * <p>Ultimately we need to allow deployers to actually inject their fully-initialized {@link org.jasig.cas.client.session.SingleSignOutHandler}.</p>
         */
        if (!this.handlerInitialized.getAndSet(true)) {
            handler.init();
        }

        if (handler.process(request, response)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    public void destroy() {
    }
}
