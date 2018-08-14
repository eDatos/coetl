package es.gobcan.coetl.pentaho.enumeration;

public enum JobMethodPentahoServiceEnum implements CarteMethodPentahoServiceEnum {

    STATUS("jobStatus/"), REGISTER("registerJob/");

    private String resource;

    private JobMethodPentahoServiceEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
