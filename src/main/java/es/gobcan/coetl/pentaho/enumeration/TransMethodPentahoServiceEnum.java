package es.gobcan.coetl.pentaho.enumeration;

public enum TransMethodPentahoServiceEnum implements CarteMethodPentahoServiceEnum {

    STATUS("transStatus/"), REGISTER("registerTrans/");

    private String resource;

    private TransMethodPentahoServiceEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
