package es.gobcan.coetl.config;

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
            cm.createCache(es.gobcan.coetl.domain.Usuario.class.getName(), jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Usuario.class.getName() + ".roles", jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Pelicula.class.getName(), jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Pelicula.class.getName() + ".idiomas", jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Pelicula.class.getName() + ".actores", jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Pelicula.class.getName() + ".categorias", jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Actor.class.getName(), jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Actor.class.getName() + ".peliculas", jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Categoria.class.getName(), jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Categoria.class.getName() + ".peliculas", jcacheConfiguration);
            cm.createCache(es.gobcan.coetl.domain.Idioma.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
