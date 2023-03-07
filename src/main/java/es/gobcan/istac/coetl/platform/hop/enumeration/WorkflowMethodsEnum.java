package es.gobcan.istac.coetl.platform.hop.enumeration;

public enum WorkflowMethodsEnum implements HopMethodsEnum {

    STATUS("workflowStatus/"), REGISTER("registerWorkflow/"), START("startWorkflow/"), REMOVE("stopWorkflow/");

    private final String resource;

    private WorkflowMethodsEnum(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return this.resource;
    }
}
