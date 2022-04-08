package es.gobcan.istac.coetl.service.impl;


import es.gobcan.istac.coetl.config.ApplicationProperties;
import es.gobcan.istac.coetl.service.DataConfigurationService;
import org.springframework.stereotype.Service;


@Service
public class DataConfigurationServiceImpl implements DataConfigurationService {
    public static final String INSTANCE_COETL = "COETL";
    public static final String INSTANCE_COETL_LAB = "COETLLAB";

    public static final String INSTANCE_METAMAC_COETL = "metamac.coetl.";
    public static final String INSTANCE_METAMAC_COETL_LAB = "metamac.coetllab.";

    public static String prefixInstance;

    private final ApplicationProperties applicationProperties;

    public DataConfigurationServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        if(applicationProperties.getInstallation().getInstance().equalsIgnoreCase(INSTANCE_COETL_LAB)){
            this.prefixInstance = INSTANCE_METAMAC_COETL_LAB;
        } else {
            this.prefixInstance = INSTANCE_METAMAC_COETL;
        }
    }

    private String getPrefixInstace(){
        return prefixInstance;
    }
    public String getDB_URL(){
        return getPrefixInstace() + "db.url";
    }
    public String getDB_USERNAME(){
        return getPrefixInstace() + "db.username";
    }
    public String getDB_PASSWORD(){
        return getPrefixInstace() + "db.password";
    }
    public String getDB_DRIVER_NAME(){
        return getPrefixInstace() + "db.driver_name";
    }

    /*GIT*/
    public String getMETAMAC_KEY_GIT_USER(){
        return getPrefixInstace() + "git.username";
    }

    public String getMETAMAC_KEY_GIT_PASSWORD(){
        return getPrefixInstace() + "git.password";
    }

    public String getMETAMAC_KEY_GIT_BRANCH(){
        return getPrefixInstace() + "git.branch";
    }

/*PENTAHO*/
    public String getMETAMAC_KEY_PENTAHO_ENDPOINT(){
        return getPrefixInstace() + "pentaho.endpoint";
    }

    public String getMETAMAC_KEY_PENTAHO_AUTH_USER(){
        return getPrefixInstace() + "pentaho.auth.user";
    }

    public String getMETAMAC_KEY_PENTAHO_AUTH_PASSWORD(){
        return getPrefixInstace() + "pentaho.auth.password";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_OS(){
        return getPrefixInstace() + "pentaho.host.os";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_ADDRESS(){
        return getPrefixInstace() + "pentaho.host.address";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_USERNAME(){
        return getPrefixInstace() + "pentaho.host.username";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_PASSWORD(){
        return getPrefixInstace() + "pentaho.host.password";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_SUDOUSERNAME(){
        return getPrefixInstace() + "pentaho.host.sudo.username";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD(){
        return getPrefixInstace() + "pentaho.host.sudo.password";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD_PROMPTREGEX(){
        return getPrefixInstace() + "pentaho.host.sudoPasswordPromptRegex";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_SFTPPATH(){
        return getPrefixInstace() + "pentaho.host.sftpPath";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_RESOURCESPATH(){
        return getPrefixInstace() + "pentaho.host.resourcesPath";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_OWNERUSERRESOURCESPATH(){
        return getPrefixInstace() + "pentaho.host.ownerUserResourcesPath";
    }

    public String getMETAMAC_KEY_PENTAHO_HOST_OWNERGROUPRESOURCESPATH(){
        return getPrefixInstace() + "pentaho.host.ownerGroupResourcesPath";
    }

    public String getMETAMAC_KEY_PENTAHO_MAIN_RESOURCE_PREFIX(){
        return getPrefixInstace() + "pentaho.host.mainResourcePrefix";
    }
}
