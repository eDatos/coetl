package es.gobcan.istac.coetl.pentaho.enumeration;

public enum JobMethodsEnum implements CarteMethodsEnum {

    STATUS("jobStatus/"), REGISTER("registerJob/"), START("startJob/"), REMOVE("removeJob/");

    private final String resource;

    private JobMethodsEnum(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return this.resource;
    }
}
