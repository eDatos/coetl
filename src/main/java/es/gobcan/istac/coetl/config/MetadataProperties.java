package es.gobcan.istac.coetl.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.service.MetadataConfigurationService;

@Component("metadataProperties")
public class MetadataProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataConfigurationService configurationService;

    private String metamacNavbar;
    private String metamacCasPrefix;
    private String metamacCasLoginUrl;
    private String metamacCasLogoutUrl;
 
    @PostConstruct
    public void setValues() {
        try {
            metamacNavbar = normalizeUrl(configurationService.retrieveNavbarUrl());
            metamacCasPrefix = normalizeUrl(configurationService.retrieveSecurityCasServerUrlPrefix());
            metamacCasLoginUrl = normalizeUrl(configurationService.retrieveSecurityCasServiceLoginUrl());
            metamacCasLogoutUrl = normalizeUrl(configurationService.retrieveSecurityCasServiceLogoutUrl());
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }
    
    public String getMetamacNavbar() {
        return metamacNavbar;
    }

    public void setMetamacNavbar(String metamacNavbar) {
        this.metamacNavbar = metamacNavbar;
    }
        
    public String getMetamacCasPrefix() {
        return metamacCasPrefix;
    }
   
    public void setMetamacCasPrefix(String metamacCasPrefix) {
        this.metamacCasPrefix = metamacCasPrefix;
    }
    
    public String getMetamacCasLoginUrl() {
        return metamacCasLoginUrl;
    }
    
    public void setMetamacCasLoginUrl(String metamacCasLoginUrl) {
        this.metamacCasLoginUrl = metamacCasLoginUrl;
    }
    
    public String getMetamacCasLogoutUrl() {
        return metamacCasLogoutUrl;
    }
    
    public void setMetamacCasLogoutUrl(String metamacCasLogoutUrl) {
        this.metamacCasLogoutUrl = metamacCasLogoutUrl;
    }

    private String normalizeUrl(String url) {
        url = StringUtils.removeEnd(url, "/");
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://"+url;
        }
        return url;
    }
}
