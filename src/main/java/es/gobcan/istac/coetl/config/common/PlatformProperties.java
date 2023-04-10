package es.gobcan.istac.coetl.config.common;


public interface PlatformProperties {

    public String getEndpoint();

    public void setEndpoint(String endpoint);

    public String getMainResourcePrefix();

    public void setMainResourcePrefix(String mainResourcePrefix);

    public PlatformAuth getAuth();

    public PlatformHost getHost();
}
