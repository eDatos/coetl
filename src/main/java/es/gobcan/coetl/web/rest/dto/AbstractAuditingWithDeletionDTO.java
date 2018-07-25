package es.gobcan.coetl.web.rest.dto;

import java.time.ZonedDateTime;

public abstract class AbstractAuditingWithDeletionDTO extends AbstractAuditingDTO {

    private ZonedDateTime deletionDate;

    private String deletedBy;

    public ZonedDateTime getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(ZonedDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

}
