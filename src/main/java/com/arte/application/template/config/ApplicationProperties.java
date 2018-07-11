package com.arte.application.template.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Cas cas = new Cas();
    private final Ldap ldap = new Ldap();

    public Cas getCas() {
        return cas;
    }

    public static class Cas {

        // Required
        private String endpoint;
        private String service;

        // Optional
        private String login;
        private String logout;
        private String validate;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getLogin() {
            if (StringUtils.isEmpty(login)) {
                return StringUtils.removeEnd(endpoint, "/") + "/login";
            }
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getLogout() {
            if (StringUtils.isEmpty(logout)) {
                return StringUtils.removeEnd(endpoint, "/") + "/logout";
            }
            return logout;
        }

        public void setLogout(String logout) {
            this.logout = logout;
        }

        public String getValidate() {
            if (StringUtils.isEmpty(logout)) {
                return endpoint;
            }
            return validate;
        }

        public void setValidate(String validate) {
            this.validate = validate;
        }

    }

    public Ldap getLdap() {
        return ldap;
    }

    public static class Ldap {

        private String url;
        private String username;
        private String password;
        private String base;
        private String searchUsersProperty;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getSearchUsersProperty() {
            return searchUsersProperty;
        }

        public void setSearchUsersProperty(String searchUsersProperty) {
            this.searchUsersProperty = searchUsersProperty;
        }
    }

}
