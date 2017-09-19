package es.tenerife.secretaria.libro.config;

import java.util.concurrent.TimeUnit;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.context.annotation.Bean;

import io.github.jhipster.config.JHipsterProperties;

//@Configuration
//@EnableCaching
//@AutoConfigureAfter(value = { MetricsConfiguration.class })
//@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

	private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

	public CacheConfiguration(JHipsterProperties jHipsterProperties) {
		JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

		jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Object.class, Object.class,
						ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
				.withExpiry(
						Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
				.build());
	}

	@Bean
	public JCacheManagerCustomizer cacheManagerCustomizer() {
		return cm -> {
			cm.createCache(es.tenerife.secretaria.libro.domain.Usuario.class.getName(), jcacheConfiguration);
			cm.createCache(es.tenerife.secretaria.libro.domain.Usuario.class.getName() + ".roles", jcacheConfiguration);
			cm.createCache(es.tenerife.secretaria.libro.domain.Rol.class.getName(), jcacheConfiguration);
			cm.createCache(es.tenerife.secretaria.libro.domain.Rol.class.getName() + ".operaciones",
					jcacheConfiguration);
			cm.createCache(es.tenerife.secretaria.libro.domain.Rol.class.getName() + ".usuarios", jcacheConfiguration);
			cm.createCache(es.tenerife.secretaria.libro.domain.Operacion.class.getName(), jcacheConfiguration);
			cm.createCache(es.tenerife.secretaria.libro.domain.Operacion.class.getName() + ".roles",
					jcacheConfiguration);
			// jhipster-needle-ehcache-add-entry
		};
	}
}
