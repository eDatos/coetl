package es.gobcan.coetl.pentaho.enumeration;

public enum JobMethodsEnum implements CarteMethodsEnum {

    STATUS("jobStatus/"), REGISTER("registerJob/");

    private String resource;

    private JobMethodsEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return this.resource;
    }
}
