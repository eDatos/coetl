package es.gobcan.istac.coetl.platform.hop.enumeration;

public enum PipelineMethodsEnum implements HopMethodsEnum {

    STATUS("pipelineStatus/"), REGISTER("registerPipeline/"), PREPARE("prepareExec/"), START("startPipeline/"), REMOVE("removePipeline/");

    private final String resource;

    private PipelineMethodsEnum(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return this.resource;
    }
}
