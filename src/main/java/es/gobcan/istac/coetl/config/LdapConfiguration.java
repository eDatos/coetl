package es.gobcan.istac.coetl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

    private LdapContextSource ldapContextSource = null;

    @Bean
    public LdapContextSource contextSource() {
        if (ldapContextSource == null) {
            ldapContextSource = ldapContextSource();
        }
        return ldapContextSource;

    }

    private LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(applicationProperties.getLdap().getUrl());
        contextSource.setBase(applicationProperties.getLdap().getBase());
        contextSource.setUserDn(applicationProperties.getLdap().getUsername());
        contextSource.setPassword(applicationProperties.getLdap().getPassword());
        contextSource.afterPropertiesSet(); // needed otherwise you will have a NullPointerException in spring
        return contextSource;
    }

}
