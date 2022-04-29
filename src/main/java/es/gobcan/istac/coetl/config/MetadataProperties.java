package es.gobcan.istac.coetl.config;

import es.gobcan.istac.coetl.service.DataConfigurationService;
import es.gobcan.istac.coetl.service.MetadataConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("metadataProperties")
public class MetadataProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataConfigurationService configurationService;

    @Autowired
    private DataConfigurationService instanceService;

    private String metamacNavbar;
    private String metamacCasPrefix;
    private String metamacCasLoginUrl;
    private String metamacCasLogoutUrl;
    private String casService;

    @PostConstruct
    public void setValues() {
        try {
            metamacNavbar = normalizeUrl(configurationService.retrieveNavbarUrl());
            metamacCasPrefix = normalizeUrl(configurationService.retrieveSecurityCasServerUrlPrefix());
            metamacCasLoginUrl = normalizeUrl(configurationService.retrieveSecurityCasServiceLoginUrl());
            metamacCasLogoutUrl = normalizeUrl(configurationService.retrieveSecurityCasServiceLogoutUrl());
            casService = normalizeUrl(configurationService.findProperty(instanceService.getCasService()));
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }

    public String getMetamacNavbar() {
        return metamacNavbar;
    }

    public String getMetamacCasPrefix() {
        return metamacCasPrefix;
    }

    public String getMetamacCasLoginUrl() {
        return metamacCasLoginUrl;
    }

    public String getMetamacCasLogoutUrl() {
        return metamacCasLogoutUrl;
    }

    public String getCasService() {
        return casService;
    }

    public void setCasService(String casService) {
        this.casService = casService;
    }

    private String normalizeUrl(String url) {
        url = StringUtils.removeEnd(url, "/");
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://"+url;
        }
        return url;
    }
}
