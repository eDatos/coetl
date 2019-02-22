package es.gobcan.coetl.web.rest.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractVersionedAndAuditingWithDeletionDTO extends AbstractVersionedAndAuditingDTO {

    private Instant deletionDate;

    private String deletedBy;

    @JsonProperty
    public Instant getDeletionDate() {
        return deletionDate;
    }

    @JsonIgnore
    public void setDeletionDate(Instant deletionDate) {
        this.deletionDate = deletionDate;
    }

    @JsonProperty
    public String getDeletedBy() {
        return deletedBy;
    }

    @JsonIgnore
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}