package es.gobcan.istac.coetl.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.service.MetadataConfigurationService;

@Component
public class PentahoProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private static final String METAMAC_KEY_PENTAHO_ENDPOINT = "metamac.coetl.pentaho.endpoint";
    private static final String METAMAC_KEY_PENTAHO_AUTH_USER = "metamac.coetl.pentaho.auth.user";
    private static final String METAMAC_KEY_PENTAHO_AUTH_PASSWORD = "metamac.coetl.pentaho.auth.password";
    private static final String METAMAC_KEY_PENTAHO_HOST_OS = "metamac.coetl.pentaho.host.os";
    private static final String METAMAC_KEY_PENTAHO_HOST_ADDRESS = "metamac.coetl.pentaho.host.address";
    private static final String METAMAC_KEY_PENTAHO_HOST_USERNAME = "metamac.coetl.pentaho.host.username";
    private static final String METAMAC_KEY_PENTAHO_HOST_PASSWORD = "metamac.coetl.pentaho.host.password";
    private static final String METAMAC_KEY_PENTAHO_HOST_SUDOUSERNAME = "metamac.coetl.pentaho.host.sudo.username";
    private static final String METAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD = "metamac.coetl.pentaho.host.sudo.password";
    private static final String METAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD_PROMPTREGEX = "metamac.coetl.pentaho.host.sudoPasswordPromptRegex";
    private static final String METAMAC_KEY_PENTAHO_HOST_SFTPPATH = "metamac.coetl.pentaho.host.sftpPath";
    private static final String METAMAC_KEY_PENTAHO_HOST_RESOURCESPATH = "metamac.coetl.pentaho.host.resourcesPath";
    private static final String METAMAC_KEY_PENTAHO_HOST_OWNERUSERRESOURCESPATH = "metamac.coetl.pentaho.host.ownerUserResourcesPath";
    private static final String METAMAC_KEY_PENTAHO_HOST_OWNERGROUPRESOURCESPATH = "metamac.coetl.pentaho.host.ownerGroupResourcesPath";
    private static final String METAMAC_KEY_PENTAHO_MAIN_RESOURCE_PREFIX = "metamac.coetl.pentaho.mainResourcePrefix";
    
    private String endpoint = StringUtils.EMPTY;
    private String mainResourcePrefix = StringUtils.EMPTY;
    private final Auth auth = new Auth();
    private final Host host = new Host();
    
    @Autowired
    private  MetadataConfigurationService configurationService;
    
    @PostConstruct
    public void setValues() {
        try {
            setEndpoint(configurationService.findProperty(METAMAC_KEY_PENTAHO_ENDPOINT));
            setMainResourcePrefix(configurationService.findProperty(METAMAC_KEY_PENTAHO_MAIN_RESOURCE_PREFIX));
            auth.setUser(configurationService.findProperty(METAMAC_KEY_PENTAHO_AUTH_USER));
            auth.setPassword(configurationService.findProperty(METAMAC_KEY_PENTAHO_AUTH_PASSWORD));
            host.setOs(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_OS));
            host.setAddress(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_ADDRESS));
            host.setUsername(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_USERNAME));
            host.setPassword(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_PASSWORD));
            host.setSudoUsername(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_SUDOUSERNAME));
            host.setSudoPassword(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD));
            host.setSudoPasswordPromptRegex(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD_PROMPTREGEX));
            host.setSftpPath(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_SFTPPATH));
            host.setResourcesPath(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_RESOURCESPATH));
            host.setOwnerUserResourcesPath(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_OWNERUSERRESOURCESPATH));
            host.setOwnerGroupResourcesPath(configurationService.findProperty(METAMAC_KEY_PENTAHO_HOST_OWNERGROUPRESOURCESPATH));
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMainResourcePrefix() {
        return mainResourcePrefix;
    }

    public void setMainResourcePrefix(String mainResourcePrefix) {
        this.mainResourcePrefix = mainResourcePrefix;
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
