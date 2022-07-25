package es.gobcan.istac.coetl.config;

import com.arte.libs.grammar.repository.support.ArteJpaRepositoryFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import es.gobcan.istac.coetl.config.annotations.ConditionalOnMissingProperty;
import es.gobcan.istac.coetl.service.DataConfigurationService;
import es.gobcan.istac.coetl.service.MetadataConfigurationService;
import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.liquibase.AsyncSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "es.gobcan.istac.coetl.repository", repositoryFactoryBeanClass = ArteJpaRepositoryFactoryBean.class)
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final Environment env;

    @Autowired
    private DataConfigurationService dataService;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
    }

    @ConditionalOnMissingProperty(prefix="spring.datasource", value={"url", "username", "password"})
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(destroyMethod = "close")
    @Primary
    public DataSource dataSource(MetadataConfigurationService metadataConfigurationService) {
        // @formatter:off
        HikariDataSource hikariDataSource = (HikariDataSource) DataSourceBuilder.create().
                url(metadataConfigurationService.retrieveProperty(dataService.getDbUrl())).
                driverClassName(metadataConfigurationService.retrieveProperty(dataService.getDdDriverName())).
                username(metadataConfigurationService.findProperty(dataService.getDbUsername())).
                password(metadataConfigurationService.findProperty(dataService.getDbPassword())).
                type(HikariDataSource.class).
                build();
        // @formatter:on

        hikariDataSource.setMaximumPoolSize(5);
        return hikariDataSource;
    }

    @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") TaskExecutor taskExecutor, DataSource dataSource, LiquibaseProperties liquibaseProperties) {

        // Use liquibase.integration.spring.SpringLiquibase if you don't want Liquibase
        // to start asynchronously
        SpringLiquibase liquibase = new AsyncSpringLiquibase(taskExecutor, env);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        if (env.acceptsProfiles(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE)) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.debug("Configurando Liquibase");
        }
        return liquibase;
    }
}
