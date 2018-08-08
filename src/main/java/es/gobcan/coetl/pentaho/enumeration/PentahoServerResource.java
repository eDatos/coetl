package es.gobcan.coetl.pentaho.enumeration;

public enum PentahoServerResource implements PentahoCarteResource {

    STATUS("status");

    private String resource;

    private PentahoServerResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
