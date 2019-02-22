package es.gobcan.istac.coetl.pentaho.enumeration;

public enum ServerMethodsEnum implements CarteMethodsEnum {

    STATUS("status");

    private String resource;

    private ServerMethodsEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
