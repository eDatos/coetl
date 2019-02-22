package es.gobcan.istac.coetl.config.flush;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

/**
 * Hibernate Jpa Dialect extension than avoid change the flush mode
 */
public class NoFlushModeHibernateJpaDialect extends HibernateJpaDialect {

    private static final long serialVersionUID = -6734786149088803173L;

    @Override
    protected FlushMode prepareFlushMode(Session session, boolean readOnly) {
        return null; // No FlushMode change needed
    }
}