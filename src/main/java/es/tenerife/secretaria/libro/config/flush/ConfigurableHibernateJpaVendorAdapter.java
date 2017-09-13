package es.tenerife.secretaria.libro.config.flush;

import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Hibernate Jpa Vendor Adapter extension to allow select the JpaDialect.
 */
public class ConfigurableHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter {

	private HibernateJpaDialect jpaDialect;

	@Override
	public HibernateJpaDialect getJpaDialect() {
		return jpaDialect != null ? jpaDialect : super.getJpaDialect();
	}

	public void setJpaDialect(HibernateJpaDialect jpaDialect) {
		this.jpaDialect = jpaDialect;
	}
}