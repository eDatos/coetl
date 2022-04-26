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
        if(INSTANCE_COETL_LAB.equalsIgnoreCase(applicationProperties.getInstallation().getInstance())){
            this.prefixInstance = INSTANCE_METAMAC_COETL_LAB;
        } else {
            this.prefixInstance = INSTANCE_METAMAC_COETL;
        }
    }

    private String getPrefixInstace(){
        return prefixInstance;
    }

    /*CAS*/
    public String getCasService(){
        return getPrefixInstace() + "cas.service";
    }

    /*DB*/
    public String getDbUrl(){
        return getPrefixInstace() + "db.url";
    }
    public String getDbUsername(){
        return getPrefixInstace() + "db.username";
    }
    public String getDbPassword(){
        return getPrefixInstace() + "db.password";
    }
    public String getDdDriverName(){
        return getPrefixInstace() + "db.driver_name";
    }

    /*GIT*/
    public String getMetamacKeyGitUser(){
        return getPrefixInstace() + "git.username";
    }

    public String getMetamacKeyGitPassword(){
        return getPrefixInstace() + "git.password";
    }

    public String getMetamacKeyGitBranch(){
        return getPrefixInstace() + "git.branch";
    }

/*PENTAHO*/
    public String getMetamacKeyPentahoEndpoint(){
        return getPrefixInstace() + "pentaho.endpoint";
    }

    public String getMetamacKeyPentahoAuthUser(){
        return getPrefixInstace() + "pentaho.auth.user";
    }

    public String getMetamacKeyPentahoAuthPassword(){
        return getPrefixInstace() + "pentaho.auth.password";
    }

    public String getMetamacKeyPentahoHostOs(){
        return getPrefixInstace() + "pentaho.host.os";
    }

    public String getMetamacKeyPentahoHostAddress(){
        return getPrefixInstace() + "pentaho.host.address";
    }

    public String getMetamacKeyPentahoHostUsername(){
        return getPrefixInstace() + "pentaho.host.username";
    }

    public String getMetamacKeyPentahoHostPassword(){
        return getPrefixInstace() + "pentaho.host.password";
    }

    public String getMetamacKeyPentahoHostSudoUsername(){
        return getPrefixInstace() + "pentaho.host.sudo.username";
    }

    public String getMetamacKeyPentahoHostSudopassword(){
        return getPrefixInstace() + "pentaho.host.sudo.password";
    }

    public String getMetamacKeyPentahoHostSudoPasswordProptRegex(){
        return getPrefixInstace() + "pentaho.host.sudoPasswordPromptRegex";
    }

    public String getMetamacKeyPentahoHostSftpPath(){
        return getPrefixInstace() + "pentaho.host.sftpPath";
    }

    public String getMetamacKeyPentahoHostResourcesPath(){
        return getPrefixInstace() + "pentaho.host.resourcesPath";
    }

    public String getMetamacKeyPentahoHostOwnerUserResourcesPath(){
        return getPrefixInstace() + "pentaho.host.ownerUserResourcesPath";
    }

    public String getMetamacKeyPentahoHostOwnerGroupResourcesPath(){
        return getPrefixInstace() + "pentaho.host.ownerGroupResourcesPath";
    }

    public String getMetamacKeyPentahoMainResourcePrefix(){
        return getPrefixInstace() + "pentaho.mainResourcePrefix";
    }
}
