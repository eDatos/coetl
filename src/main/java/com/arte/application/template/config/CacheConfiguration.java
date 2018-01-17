package com.arte.application.template.config;

import java.util.concurrent.TimeUnit;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.context.annotation.Bean;

import io.github.jhipster.config.JHipsterProperties;

/// Desactivado para evitar el uso de cache
// @Configuration
// @EnableCaching
// @AutoConfigureAfter(value = { MetricsConfiguration.class })
// @AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration
                .fromEhcacheCacheConfiguration(CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS))).build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.arte.application.template.domain.Usuario.class.getName(), jcacheConfiguration);
            cm.createCache(com.arte.application.template.domain.Usuario.class.getName() + ".roles", jcacheConfiguration);
            cm.createCache(com.arte.application.template.domain.Rol.class.getName(), jcacheConfiguration);
            cm.createCache(com.arte.application.template.domain.Rol.class.getName() + ".operaciones", jcacheConfiguration);
            cm.createCache(com.arte.application.template.domain.Rol.class.getName() + ".usuarios", jcacheConfiguration);
            cm.createCache(com.arte.application.template.domain.Operacion.class.getName(), jcacheConfiguration);
            cm.createCache(com.arte.application.template.domain.Operacion.class.getName() + ".roles", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
