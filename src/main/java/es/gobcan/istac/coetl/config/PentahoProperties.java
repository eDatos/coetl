package es.gobcan.istac.coetl.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pentaho", ignoreUnknownFields = false)
public class PentahoProperties {

    private String endpoint = StringUtils.EMPTY;
    private final Auth auth = new Auth();
    private final Host host = new Host();

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Auth getAuth() {
        return auth;
    }

    public Host getHost() {
        return host;
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

    public static class Host {

        private String address = StringUtils.EMPTY;
        private String username = StringUtils.EMPTY;
        private String password = StringUtils.EMPTY;
        private String sudoUsername = StringUtils.EMPTY;
        private String sudoPassword = StringUtils.EMPTY;
        private String sudoPasswordPromptRegex = StringUtils.EMPTY;
        private String os = StringUtils.EMPTY;
        private String sftpPath = StringUtils.EMPTY;
        private String resourcesPath = StringUtils.EMPTY;
        private String ownerUserResourcesPath = StringUtils.EMPTY;
        private String ownerGroupResourcesPath = StringUtils.EMPTY;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getSudoUsername() {
            return sudoUsername;
        }

        public void setSudoUsername(String sudoUsername) {
            this.sudoUsername = sudoUsername;
        }

        public String getSudoPassword() {
            return sudoPassword;
        }

        public void setSudoPassword(String sudoPassword) {
            this.sudoPassword = sudoPassword;
        }

        public String getSudoPasswordPromptRegex() {
            return sudoPasswordPromptRegex;
        }

        public void setSudoPasswordPromptRegex(String sudoPasswordPromptRegex) {
            this.sudoPasswordPromptRegex = sudoPasswordPromptRegex;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getSftpPath() {
            return sftpPath;
        }

        public void setSftpPath(String sftpPath) {
            this.sftpPath = sftpPath;
        }

        public String getResourcesPath() {
            return resourcesPath;
        }

        public void setResourcesPath(String resourcesPath) {
            this.resourcesPath = resourcesPath;
        }

        public String getOwnerUserResourcesPath() {
            return ownerUserResourcesPath;
        }

        public void setOwnerUserResourcesPath(String ownerUserResourcesPath) {
            this.ownerUserResourcesPath = ownerUserResourcesPath;
        }

        public String getOwnerGroupResourcesPath() {
            return ownerGroupResourcesPath;
        }

        public void setOwnerGroupResourcesPath(String ownerGroupResourcesPath) {
            this.ownerGroupResourcesPath = ownerGroupResourcesPath;
        }
    }
}
