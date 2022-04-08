package es.gobcan.istac.coetl.service;

public interface DataConfigurationService {

    /**DataBase*/
    public String getDB_URL();
    public String getDB_USERNAME();
    public String getDB_PASSWORD();
    public String getDB_DRIVER_NAME();

    /**GIT*/
    public String getMETAMAC_KEY_GIT_USER();
    public String getMETAMAC_KEY_GIT_PASSWORD();
    public String getMETAMAC_KEY_GIT_BRANCH();

    /**PENTAHO*/
    public String getMETAMAC_KEY_PENTAHO_ENDPOINT();
    public String getMETAMAC_KEY_PENTAHO_AUTH_USER();
    public String getMETAMAC_KEY_PENTAHO_AUTH_PASSWORD();
    public String getMETAMAC_KEY_PENTAHO_HOST_OS();
    public String getMETAMAC_KEY_PENTAHO_HOST_ADDRESS();
    public String getMETAMAC_KEY_PENTAHO_HOST_USERNAME();
    public String getMETAMAC_KEY_PENTAHO_HOST_PASSWORD();
    public String getMETAMAC_KEY_PENTAHO_HOST_SUDOUSERNAME();
    public String getMETAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD();
    public String getMETAMAC_KEY_PENTAHO_HOST_SUDOPASSWORD_PROMPTREGEX();
    public String getMETAMAC_KEY_PENTAHO_HOST_SFTPPATH();
    public String getMETAMAC_KEY_PENTAHO_HOST_RESOURCESPATH();
    public String getMETAMAC_KEY_PENTAHO_HOST_OWNERUSERRESOURCESPATH();
    public String getMETAMAC_KEY_PENTAHO_HOST_OWNERGROUPRESOURCESPATH();
    public String getMETAMAC_KEY_PENTAHO_MAIN_RESOURCE_PREFIX();
}
