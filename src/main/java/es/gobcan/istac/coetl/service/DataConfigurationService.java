package es.gobcan.istac.coetl.service;

public interface DataConfigurationService {

    /**CAS*/
    public String getCasService();
    public String getKeyCas();

    /**DataBase*/
    public String getDbUrl();
    public String getDbUsername();
    public String getDbPassword();
    public String getDdDriverName();

    /**GIT*/
    public String getMetamacKeyGitUser();
    public String getMetamacKeyGitPassword();
    public String getMetamacKeyGitBranch();

    /**PENTAHO*/
    public String getMetamacKeyPentahoEndpoint();
    public String getMetamacKeyPentahoAuthUser();
    public String getMetamacKeyPentahoAuthPassword();
    public String getMetamacKeyPentahoHostOs();
    public String getMetamacKeyPentahoHostAddress();
    public String getMetamacKeyPentahoHostUsername();
    public String getMetamacKeyPentahoHostPassword();
    public String getMetamacKeyPentahoHostSudoUsername();
    public String getMetamacKeyPentahoHostSudopassword();
    public String getMetamacKeyPentahoHostSudoPasswordProptRegex();
    public String getMetamacKeyPentahoHostSftpPath();
    public String getMetamacKeyPentahoHostResourcesPath();
    public String getMetamacKeyPentahoHostOwnerUserResourcesPath();
    public String getMetamacKeyPentahoHostOwnerGroupResourcesPath();
    public String getMetamacKeyPentahoMainResourcePrefix();
}
