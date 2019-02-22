package es.gobcan.istac.coetl.security.jwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.core.Ehcache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.cas.authentication.StatelessTicketCache;
import org.springframework.util.Assert;

/**
 * Caches tickets using a Spring IoC defined
 * <a href="http://ehcache.sourceforge.net">EHCACHE</a>.
 * Implementation for EhCache 3.2.X
 */
public class CasEhCacheBasedTicketCache implements StatelessTicketCache, InitializingBean {
    // ~ Static fields/initializers
    // =====================================================================================

    private static final Log logger = LogFactory.getLog(CasEhCacheBasedTicketCache.class);

    // ~ Instance fields
    // ================================================================================================

    private Ehcache<String, CasAuthenticationToken> cache;

    // ~ Methods
    // ========================================================================================================

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "cache mandatory");
    }

    public CasAuthenticationToken getByTicketId(final String serviceTicket) {
        final CasAuthenticationToken element = cache.get(serviceTicket);

        if (logger.isDebugEnabled()) {
            logger.debug("Cache hit: " + (element != null) + "; service ticket: " + serviceTicket);
        }

        return element == null ? null : element;
    }

    public Ehcache<String, CasAuthenticationToken> getCache() {
        return cache;
    }

    public void putTicketInCache(final CasAuthenticationToken token) {
        final String key = token.getCredentials().toString();
        final CasAuthenticationToken value = token;

        if (logger.isDebugEnabled()) {
            logger.debug("Cache put: " + key);
        }

        cache.put(key, value);
    }

    public void removeTicketFromCache(final CasAuthenticationToken token) {
        if (logger.isDebugEnabled()) {
            logger.debug("Cache remove: " + token.getCredentials().toString());
        }

        this.removeTicketFromCache(token.getCredentials().toString());
    }

    public void removeTicketFromCache(final String serviceTicket) {
        cache.remove(serviceTicket);
    }

    public void setCache(final Ehcache<String, CasAuthenticationToken> cache) {
        this.cache = cache;
    }
}
