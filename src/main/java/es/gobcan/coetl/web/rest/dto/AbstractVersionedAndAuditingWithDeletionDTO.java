package es.gobcan.coetl.web.rest.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractVersionedAndAuditingWithDeletionDTO extends AbstractVersionedAndAuditingDTO {

    private ZonedDateTime deletionDate;

    private String deletedBy;

    @JsonProperty
    public ZonedDateTime getDeletionDate() {
        return deletionDate;
    }

    @JsonIgnore
    public void setDeletionDate(ZonedDateTime deletionDate) {
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