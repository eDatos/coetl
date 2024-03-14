package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;
import java.time.Instant;

public class ComputationalThreadsBaseDTO extends AbstractVersionedAndAuditingWithDeletionDTO implements Serializable  {

    private static final long serialVersionUID = 7581847502743845396L;

    private Long id;
    private String code;
    private String name;
    private String executionDescription;
    private String executionPlanning;
    private Instant nextExecution;
    private ExternalItemDTO externalItem;

    public ComputationalThreadsBaseDTO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutionDescription() {
        return executionDescription;
    }

    public void setExecutionDescription(String executionDescription) {
        this.executionDescription = executionDescription;
    }

    public String getExecutionPlanning() {
        return executionPlanning;
    }

    public void setExecutionPlanning(String executionPlanning) {
        this.executionPlanning = executionPlanning;
    }

    public Instant getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(Instant nextExecution) {
        this.nextExecution = nextExecution;
    }

    public ExternalItemDTO getExternalItem() {
        return externalItem;
    }

    public void setExternalItem(ExternalItemDTO externalItem) {
        this.externalItem = externalItem;
    }

}
