package es.gobcan.istac.coetl.config;

import es.gobcan.istac.coetl.config.common.PlatformAuth;
import es.gobcan.istac.coetl.config.common.PlatformHost;
import es.gobcan.istac.coetl.config.common.PlatformProperties;
import es.gobcan.istac.coetl.service.MetadataConfigurationService;
import es.gobcan.istac.coetl.util.GzipUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ApacheHopProperties implements PlatformProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String endpoint = StringUtils.EMPTY;
    private String mainResourcePrefix = StringUtils.EMPTY;
    private String jsonMetadata = StringUtils.EMPTY;
    private String variablesTemplate = StringUtils.EMPTY;
    private final PlatformAuth auth = new PlatformAuth();
    private final PlatformHost host = new PlatformHost();

    @Autowired
    private MetadataConfigurationService configurationService;

    @PostConstruct
    public void setValues() {
        try {
            setEndpoint(configurationService.retrieveApacheHopEndpoint());
            setMainResourcePrefix(configurationService.retrieveApacheHopMainResourcePrefix());
            setJsonMetadata(configurationService.retrieveApacheHopJsonMetadata());
            setVariablesTemplate(configurationService.retrieveApacheHopVariables());
            auth.setUser(configurationService.retrieveApacheHopAuthUser());
            auth.setPassword(configurationService.retrieveApacheHopAuthPassword());
            host.setOs(configurationService.retrieveApacheHopHostOs());
            host.setAddress(configurationService.retrieveApacheHopHostAddress());
            host.setUsername(configurationService.retrieveApacheHopHostUsername());
            host.setPassword(configurationService.retrieveApacheHopHostPassword());
            host.setSudoUsername(configurationService.retrieveApacheHopHostSudoUsername());
            host.setSudoPassword(configurationService.retrieveApacheHopHostSudopassword());
            host.setSudoPasswordPromptRegex(configurationService.retrieveApacheHopHostSudoPasswordProptRegex());
            host.setResourcesPath(configurationService.retrieveApacheHopHostResourcesPath());
            host.setOwnerUserResourcesPath(configurationService.retrieveApacheHopHostOwnerUserResourcesPath());
            host.setOwnerGroupResourcesPath(configurationService.retrieveApacheHopHostOwnerGroupResourcesPath());
            host.setHopFolder(configurationService.retrieveApacheHopHostHopFolder());
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = GzipUtils.toGzipBase64File(jsonMetadata);
    }
    
    public String getVariablesTemplate() {
        return variablesTemplate;
    }
    
    public void setVariablesTemplate(String variablesTemplate) {
        this.variablesTemplate = variablesTemplate;
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
