package es.gobcan.istac.coetl.security.jwt;

import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.codec.binary.Base64;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.config.ApplicationProperties;
import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.security.util.SecurityCookiesUtil;
import es.gobcan.istac.coetl.service.EnabledTokenService;


/**
 * Handler made by modifying org.jasig.cas.client.session.SingleSignOutHandler in order to disable jwt when the CAS sends a 'logout' request.
 */
@Service
public class JWTSingleSignOutHandler {

    private final static int DECOMPRESSION_FACTOR = 10;

    /** Logger instance */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EnabledTokenService enabledTokenService;

    /** The name of the artifact parameter.  This is used to capture the session identifier. */
    private String artifactParameterName = Protocol.CAS2.getArtifactParameterName();

    /** Parameter name that stores logout request for back channel SLO */
    private String logoutParameterName = ConfigurationKeys.LOGOUT_PARAMETER_NAME.getDefaultValue();

    /** Parameter name that stores logout request for front channel SLO */
    private String frontLogoutParameterName = ConfigurationKeys.FRONT_LOGOUT_PARAMETER_NAME.getDefaultValue();

    /** Parameter name that stores the state of the CAS server webflow for the callback */
    private String relayStateParameterName = ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getDefaultValue();

    /** The prefix url of the CAS server */
    private String casServerUrlPrefix = "";

    private boolean artifactParameterOverPost = false;

    private List<String> safeParameters;

    private final long tokenValidityInSeconds;

    private final Environment env;

    private final ApplicationProperties applicationProperties;


    public JWTSingleSignOutHandler(JHipsterProperties jHipsterProperties, ApplicationProperties applicationProperties, Environment env, EnabledTokenService enabledTokenService) {
        this.tokenValidityInSeconds = jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.env = env;
        this.enabledTokenService = enabledTokenService;
        this.applicationProperties = applicationProperties;
    }

    public void setArtifactParameterOverPost(final boolean artifactParameterOverPost) {
        this.artifactParameterOverPost = artifactParameterOverPost;
    }

    /**
     * @param name Name of the authentication token parameter.
     */
    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    /**
     * @param name Name of parameter containing CAS logout request message for back channel SLO.
     */
    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }

    /**
     * @param casServerUrlPrefix The prefix url of the CAS server.
     */
    public void setCasServerUrlPrefix(final String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    /**
     * @param name Name of parameter containing CAS logout request message for front channel SLO.
     */
    public void setFrontLogoutParameterName(final String name) {
        this.frontLogoutParameterName = name;
    }

    /**
     * @param name Name of parameter containing the state of the CAS server webflow.
     */
    public void setRelayStateParameterName(final String name) {
        this.relayStateParameterName = name;
    }

    /**
     * Initializes the component for use.
     */
    public synchronized void init() {
        if (this.safeParameters == null) {
            CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
            CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
            CommonUtils.assertNotNull(this.frontLogoutParameterName, "frontLogoutParameterName cannot be null.");
            CommonUtils.assertNotNull(this.relayStateParameterName, "relayStateParameterName cannot be null.");
            CommonUtils.assertNotNull(this.casServerUrlPrefix, "casServerUrlPrefix cannot be null.");

            if (CommonUtils.isBlank(this.casServerUrlPrefix)) {
                logger.warn("Front Channel single sign out redirects are disabled when the 'casServerUrlPrefix' value is not set.");
            }

            if (this.artifactParameterOverPost) {
                this.safeParameters = Arrays.asList(this.logoutParameterName, this.artifactParameterName);
            } else {
                this.safeParameters = Arrays.asList(this.logoutParameterName);
            }
        }
    }

    /**
     * Determines whether the given request contains an authentication token.
     *
     * @param request HTTP reqest.
     *
     * @return True if request contains authentication token, false otherwise.
     */
    private boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName,
                this.safeParameters));
    }

    /**
     * Determines whether the given request is a CAS back channel logout request.
     *
     * @param request HTTP request.
     *
     * @return True if request is logout request, false otherwise.
     */
    private boolean isBackChannelLogoutRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod())
                && !isMultipartRequest(request)
                && CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName,
                        this.safeParameters));
    }

    /**
     * Determines whether the given request is a CAS front channel logout request.  Front Channel log out requests are only supported
     * when the 'casServerUrlPrefix' value is set.
     *
     * @param request HTTP request.
     *
     * @return True if request is logout request, false otherwise.
     */
    private boolean isFrontChannelLogoutRequest(final HttpServletRequest request) {
        return "GET".equals(request.getMethod()) && CommonUtils.isNotBlank(this.casServerUrlPrefix)
                && CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.frontLogoutParameterName));
    }

    /**
     * Process a request regarding the SLO process: record the session or destroy it.
     *
     * @param request the incoming HTTP request.
     * @param response the HTTP response.
     * @return if the request should continue to be processed.
     */
    public boolean process(final HttpServletRequest request, final HttpServletResponse response) {
        if (isTokenRequest(request)) {
            logger.trace("Received a token request");
            recordSession(request, response);
            return true;

        } else if (isBackChannelLogoutRequest(request)) {
            logger.trace("Received a back channel logout request");
            destroySession(request);
            return false;

        } else if (isFrontChannelLogoutRequest(request)) {
            logger.trace("Received a front channel logout request");
            destroySession(request);
            // redirection url to the CAS server
            final String redirectionUrl = computeRedirectionToServer(request);
            if (redirectionUrl != null) {
                CommonUtils.sendRedirect(response, redirectionUrl);
            }
            return false;
        } else {
            logger.trace("Ignoring URI for logout: {}", request.getRequestURI());
            return true;
        }
    }

    /**
     * Associates a token request with the current HTTP session by recording the mapping
     * in the the configured {@link SessionMappingStorage} container.
     *
     * @param request HTTP request containing an authentication token.
     */
    private void recordSession(final HttpServletRequest request, final HttpServletResponse response) {
        final String serviceTicket = CommonUtils.safeGetParameter(request, this.artifactParameterName, this.safeParameters);

        request.setAttribute(SecurityCookiesUtil.SERVICE_TICKET_COOKIE, serviceTicket);

        Cookie cookie = new Cookie(SecurityCookiesUtil.SERVICE_TICKET_COOKIE, serviceTicket);
        cookie.setSecure(env.acceptsProfiles(Constants.SPRING_PROFILE_ENV));
        cookie.setHttpOnly(true);
        cookie.setPath(SecurityCookiesUtil.getCookiePath());
        cookie.setMaxAge((int) tokenValidityInSeconds);
        response.addCookie(cookie);
    }

    /**
     * Uncompress a logout message (base64 + deflate).
     *
     * @param originalMessage the original logout message.
     * @return the uncompressed logout message.
     */
    private String uncompressLogoutMessage(final String originalMessage) {
        final byte[] binaryMessage = Base64.decodeBase64(originalMessage);

        Inflater decompresser = null;
        try {
            // decompress the bytes
            decompresser = new Inflater();
            decompresser.setInput(binaryMessage);
            final byte[] result = new byte[binaryMessage.length * DECOMPRESSION_FACTOR];

            final int resultLength = decompresser.inflate(result);

            // decode the bytes into a String
            return new String(result, 0, resultLength, "UTF-8");
        } catch (final Exception e) {
            logger.error("Unable to decompress logout message", e);
            throw new RuntimeException(e);
        } finally {
            if (decompresser != null) {
                decompresser.end();
            }
        }
    }

    /**
     * Destroys the current HTTP session for the given CAS logout request.
     *
     * @param request HTTP request containing a CAS logout message.
     */
    private void destroySession(final HttpServletRequest request) {
        final String logoutMessage;
        // front channel logout -> the message needs to be base64 decoded + decompressed
        if (isFrontChannelLogoutRequest(request)) {
            logoutMessage = uncompressLogoutMessage(CommonUtils.safeGetParameter(request,
                    this.frontLogoutParameterName));
        } else {
            logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName, this.safeParameters);
        }
        logger.trace("Logout request:\n{}", logoutMessage);

        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
        enabledTokenService.deleteByServiceTicket(token);
    }

    /**
     * Compute the redirection url to the CAS server when it's a front channel SLO
     * (depending on the relay state parameter).
     *
     * @param request The HTTP request.
     * @return the redirection url to the CAS server.
     */
    private String computeRedirectionToServer(final HttpServletRequest request) {
        final String relayStateValue = CommonUtils.safeGetParameter(request, this.relayStateParameterName);
        // if we have a state value -> redirect to the CAS server to continue the logout process
        if (CommonUtils.isNotBlank(relayStateValue)) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append(casServerUrlPrefix);
            if (!this.casServerUrlPrefix.endsWith("/")) {
                buffer.append("/");
            }
            buffer.append("logout?_eventId=next&");
            buffer.append(this.relayStateParameterName);
            buffer.append("=");
            buffer.append(CommonUtils.urlEncode(relayStateValue));
            final String redirectUrl = buffer.toString();
            logger.debug("Redirection url to the CAS server: {}", redirectUrl);
            return redirectUrl;
        }
        return null;
    }

    private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }
}
