package es.gobcan.istac.coetl.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pentaho", ignoreUnknownFields = false)
public class PentahoProperties {

    private String endpoint = StringUtils.EMPTY;
    private final Auth auth = new Auth();

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Auth getAuth() {
        return auth;
    }

    public static class Auth {

        private String user = StringUtils.EMPTY;
        private String password = StringUtils.EMPTY;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
