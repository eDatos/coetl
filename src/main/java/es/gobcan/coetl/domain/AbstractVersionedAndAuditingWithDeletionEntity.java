package es.gobcan.coetl.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import es.gobcan.coetl.optimistic.OptimisticLockChecker;

@MappedSuperclass
@EntityListeners(OptimisticLockChecker.class)
public abstract class AbstractVersionedAndAuditingWithDeletionEntity extends AbstractVersionedAndAuditingEntity {

    private static final long serialVersionUID = 2035689875143963693L;

    @Column(name = "deletion_date")
    private ZonedDateTime deletionDate;

    @Column(name = "deleted_by", length = 50)
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

    public boolean isDeleted() {
        return deletionDate != null;
    }
}