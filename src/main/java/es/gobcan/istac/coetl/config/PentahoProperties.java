package es.gobcan.istac.coetl.config;

import es.gobcan.istac.coetl.config.common.PlatformAuth;
import es.gobcan.istac.coetl.config.common.PlatformHost;
import es.gobcan.istac.coetl.config.common.PlatformProperties;
import es.gobcan.istac.coetl.service.DataConfigurationService;
import es.gobcan.istac.coetl.service.MetadataConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PentahoProperties implements PlatformProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String endpoint = StringUtils.EMPTY;
    private String mainResourcePrefix = StringUtils.EMPTY;
    private final PlatformAuth auth = new PlatformAuth();
    private final PlatformHost host = new PlatformHost();

    @Autowired
    private MetadataConfigurationService configurationService;

    @Autowired
    private DataConfigurationService instanceService;

    @PostConstruct
    public void setValues() {
        try {
            setEndpoint(configurationService.findProperty(instanceService.getMetamacKeyPentahoEndpoint()));
            setMainResourcePrefix(configurationService.findProperty(instanceService.getMetamacKeyPentahoMainResourcePrefix()));
            auth.setUser(configurationService.findProperty(instanceService.getMetamacKeyPentahoAuthUser()));
            auth.setPassword(configurationService.findProperty(instanceService.getMetamacKeyPentahoAuthPassword()));
            host.setOs(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostOs()));
            host.setAddress(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostAddress()));
            host.setUsername(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostUsername()));
            host.setPassword(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostPassword()));
            host.setSudoUsername(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostSudoUsername()));
            host.setSudoPassword(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostSudopassword()));
            host.setSudoPasswordPromptRegex(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostSudoPasswordProptRegex()));
            host.setSftpPath(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostSftpPath()));
            host.setResourcesPath(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostResourcesPath()));
            host.setOwnerUserResourcesPath(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostOwnerUserResourcesPath()));
            host.setOwnerGroupResourcesPath(configurationService.findProperty(instanceService.getMetamacKeyPentahoHostOwnerGroupResourcesPath()));
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

    public PlatformAuth getAuth() {
        return auth;
    }

    public PlatformHost getHost() {
        return host;
    }

}
