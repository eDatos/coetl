package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class EtlDTO extends EtlBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String purpose;
    private String functionalInCharge;
    private String technicalInCharge;
    private String comments;
    private String executionDescription;
    private Instant nextExecution;
    private FileDTO etlFile;
    private FileDTO etlDescriptionFile;
    private List<FileDTO> attachedFiles;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public Instant getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(Instant nextExecution) {
        this.nextExecution = nextExecution;
    }

    public FileDTO getEtlFile() {
        return etlFile;
    }

    public void setEtlFile(FileDTO etlFile) {
        this.etlFile = etlFile;
    }

    public FileDTO getEtlDescriptionFile() {
        return etlDescriptionFile;
    }

    public void setEtlDescriptionFile(FileDTO etlDescriptionFile) {
        this.etlDescriptionFile = etlDescriptionFile;
    }

    public List<FileDTO> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<FileDTO> attachedFiles) {
        this.attachedFiles = attachedFiles;
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
