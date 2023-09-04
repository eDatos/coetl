package es.gobcan.istac.coetl.platform.pentaho.enumeration;

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
