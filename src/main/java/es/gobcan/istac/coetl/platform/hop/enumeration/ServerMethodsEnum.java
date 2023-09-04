package es.gobcan.istac.coetl.platform.hop.enumeration;

public enum ServerMethodsEnum implements HopMethodsEnum {

    STATUS("status");

    private String resource;

    private ServerMethodsEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
