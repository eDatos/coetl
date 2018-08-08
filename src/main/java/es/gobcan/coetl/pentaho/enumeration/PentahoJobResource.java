package es.gobcan.coetl.pentaho.enumeration;

public enum PentahoJobResource implements PentahoCarteResource {

    STATUS("jobStatus/"), REGISTER_JOB("registerJob/");

    private String resource;

    private PentahoJobResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
