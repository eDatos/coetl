package es.gobcan.istac.coetl.service;

import org.siemac.edatos.core.common.conf.ConfigurationService;

public interface MetadataConfigurationService extends ConfigurationService {
    
    /**CAS*/
    public String retrieveCasService();
    public String retrieveKeyCas();

    /**DataBase*/
    public String retrieveDbUrl();
    public String retrieveDbUsername();
    public String retrieveDbPassword();
    public String retrieveDdDriverName();

    /**GIT*/
    public String retrieveGitUser();
    public String retrieveGitPassword();
    public String retrieveGitBranch();

    /**PENTAHO*/
    public String retrievePentahoEndpoint();
    public String retrievePentahoAuthUser();
    public String retrievePentahoAuthPassword();
    public String retrievePentahoHostOs();
    public String retrievePentahoHostAddress();
    public String retrievePentahoHostUsername();
    public String retrievePentahoHostPassword();
    public String retrievePentahoHostSudoUsername();
    public String retrievePentahoHostSudopassword();
    public String retrievePentahoHostSudoPasswordProptRegex();
    public String retrievePentahoHostSftpPath();
    public String retrievePentahoHostResourcesPath();
    public String retrievePentahoHostOwnerUserResourcesPath();
    public String retrievePentahoHostOwnerGroupResourcesPath();
    public String retrievePentahoMainResourcePrefix();
    
    /** APACHE HOP */
    public String retrieveApacheHopEndpoint();
    public String retrieveApacheHopAuthUser();
    public String retrieveApacheHopAuthPassword();
    public String retrieveApacheHopHostOs();
    public String retrieveApacheHopHostAddress();
    public String retrieveApacheHopHostUsername();
    public String retrieveApacheHopHostPassword();
    public String retrieveApacheHopHostSudoUsername();
    public String retrieveApacheHopHostSudopassword();
    public String retrieveApacheHopHostSudoPasswordProptRegex();
    public String retrieveApacheHopHostResourcesPath();
    public String retrieveApacheHopHostOwnerUserResourcesPath();
    public String retrieveApacheHopHostOwnerGroupResourcesPath();
    public String retrieveApacheHopMainResourcePrefix();
    public String retrieveApacheHopJsonMetadata();
    
}
