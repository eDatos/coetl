package es.gobcan.istac.coetl.config;

import java.net.MalformedURLException;
import java.net.URL;

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
 
    @PostConstruct
    public void setValues() {
        try {
            metamacNavbar = normalizeUrl(configurationService.retrieveNavbarUrl());
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

    private String normalizeUrl(String url) {
        url = StringUtils.removeEnd(url, "/");
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://"+url;
        }
        return url;
    }
}
