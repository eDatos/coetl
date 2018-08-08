package es.gobcan.coetl.pentaho.enumeration;

public enum PentahoTransResource implements PentahoCarteResource {

    STATUS("transStatus/"), REGISTER_TRANS("registerTrans/");

    private String resource;

    private PentahoTransResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
