package es.gobcan.coetl.pentaho.enumeration;

public enum ServerMethodPentahoServiceEnum implements CarteMethodPentahoServiceEnum {

    STATUS("status");

    private String resource;

    private ServerMethodPentahoServiceEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
