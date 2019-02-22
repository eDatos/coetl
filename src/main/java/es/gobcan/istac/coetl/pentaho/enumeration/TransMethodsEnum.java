package es.gobcan.istac.coetl.pentaho.enumeration;

public enum TransMethodsEnum implements CarteMethodsEnum {

    STATUS("transStatus/"), REGISTER("registerTrans/"), PREPARE("prepareExec/"), START("startExec/"), REMOVE("removeTrans/");

    private String resource;

    private TransMethodsEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
