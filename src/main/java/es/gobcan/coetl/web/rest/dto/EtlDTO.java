package es.gobcan.coetl.web.rest.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import es.gobcan.coetl.domain.Etl.Type;

public class EtlDTO extends AbstractVersionedAndAuditingWithDeletionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String purpose;

    @NotBlank
    private String organizationInCharge;

    @NotBlank
    private String functionalInCharge;

    @NotBlank
    private String technicalInCharge;

    @NotNull
    private Type type;

    private String comments;

    private String executionDescription;

    private String executionPlanning;

    private ZonedDateTime nextExecution;

    @NotNull
    private FileDTO etlFile;

    @NotNull
    private FileDTO descriptionFile;

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOrganizationInCharge() {
        return organizationInCharge;
    }

    public void setOrganizationInCharge(String organizationInCharge) {
        this.organizationInCharge = organizationInCharge;
    }

    public String getFunctionalInCharge() {
        return functionalInCharge;
    }

    public void setFunctionalInCharge(String functionalInCharge) {
        this.functionalInCharge = functionalInCharge;
    }

    public String getTechnicalInCharge() {
        return technicalInCharge;
    }

    public void setTechnicalInCharge(String technicalInCharge) {
        this.technicalInCharge = technicalInCharge;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public ZonedDateTime getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(ZonedDateTime nextExecution) {
        this.nextExecution = nextExecution;
    }

    public FileDTO getEtlFile() {
        return etlFile;
    }

    public void setEtlFile(FileDTO etlFile) {
        this.etlFile = etlFile;
    }

    public FileDTO getDescriptionFile() {
        return descriptionFile;
    }

    public void setDescriptionFile(FileDTO descriptionFile) {
        this.descriptionFile = descriptionFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EtlDTO etlDTO = (EtlDTO) o;
        return !(etlDTO.getId() == null || getId() == null) && Objects.equals(getId(), etlDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        //@formatter:off
        return "EtlDTO (" +
                    "id = " + getId() + 
                    ", code = " + getCode() + 
                    ", name = " + getName() + 
                    ", type = " + getType() + 
                ")";
        //@formatter:on
    }
}
