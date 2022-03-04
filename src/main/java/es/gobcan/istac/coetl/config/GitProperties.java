package es.gobcan.istac.coetl.config;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.service.MetadataConfigurationService;

@Component
public class GitProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private static final String METAMAC_KEY_GIT_USER = "metamac.coetl.git.username";
    private static final String METAMAC_KEY_GIT_PASSWORD = "metamac.coetl.git.password";
    private static final String METAMAC_KEY_GIT_BRANCH = "metamac.coetl.git.branch";
    
    
    private String username = StringUtils.EMPTY;
    private String password = StringUtils.EMPTY;
    private String branch = StringUtils.EMPTY;

    
    @Autowired
    private  MetadataConfigurationService configurationService;
    
    @PostConstruct
    public void setValues() {
        try {
            setUsername(configurationService.findProperty(METAMAC_KEY_GIT_USER));
            setPassword(configurationService.findProperty(METAMAC_KEY_GIT_PASSWORD));
            setBranch(configurationService.findProperty(METAMAC_KEY_GIT_BRANCH));
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
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
 
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
