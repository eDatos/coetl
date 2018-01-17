package com.arte.application.template.config.flush;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Se sobreescribe la configuración por defecto de Spring Boot para evitar que
 * automaticamente cambie el flush mode.
 */
@Configuration
public class JpaConfiguration {

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    public ConfigurableHibernateJpaVendorAdapter jpaVendorAdapter() {
        ConfigurableHibernateJpaVendorAdapter adapter = new ConfigurableHibernateJpaVendorAdapter();
        adapter.setJpaDialect(new NoFlushModeHibernateJpaDialect());
        adapter.setShowSql(jpaProperties.isShowSql());
        adapter.setDatabase(jpaProperties.getDatabase());
        adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return adapter;
    }
}
