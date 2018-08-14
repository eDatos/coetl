package es.gobcan.coetl.pentaho.enumeration;

public enum TransMethodsEnum implements CarteMethodsEnum {

    STATUS("transStatus/"), REGISTER("registerTrans/");

    private String resource;

    private TransMethodsEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
