package es.gobcan.istac.coetl.config;

import es.gobcan.istac.coetl.config.common.PlatformAuth;
import es.gobcan.istac.coetl.config.common.PlatformHost;
import es.gobcan.istac.coetl.config.common.PlatformProperties;
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

    @PostConstruct
    public void setValues() {
        try {
            setEndpoint(configurationService.retrievePentahoEndpoint());
            setMainResourcePrefix(configurationService.retrievePentahoMainResourcePrefix());
            auth.setUser(configurationService.retrievePentahoAuthUser());
            auth.setPassword(configurationService.retrievePentahoAuthPassword());
            host.setOs(configurationService.retrievePentahoHostOs());
            host.setAddress(configurationService.retrievePentahoHostAddress());
            host.setUsername(configurationService.retrievePentahoHostUsername());
            host.setPassword(configurationService.retrievePentahoHostPassword());
            host.setSudoUsername(configurationService.retrievePentahoHostSudoUsername());
            host.setSudoPassword(configurationService.retrievePentahoHostSudopassword());
            host.setSudoPasswordPromptRegex(configurationService.retrievePentahoHostSudoPasswordProptRegex());
            host.setSftpPath(configurationService.retrievePentahoHostSftpPath());
            host.setResourcesPath(configurationService.retrievePentahoHostResourcesPath());
            host.setOwnerUserResourcesPath(configurationService.retrievePentahoHostOwnerUserResourcesPath());
            host.setOwnerGroupResourcesPath(configurationService.retrievePentahoHostOwnerGroupResourcesPath());
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
