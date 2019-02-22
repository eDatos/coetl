package es.gobcan.istac.coetl.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import es.gobcan.istac.coetl.optimistic.OptimisticLockChecker;

@MappedSuperclass
@EntityListeners(OptimisticLockChecker.class)
public abstract class AbstractVersionedAndAuditingWithDeletionEntity extends AbstractVersionedAndAuditingEntity {

    private static final long serialVersionUID = 2035689875143963693L;

    @Column(name = "deletion_date")
    private Instant deletionDate;

    @Column(name = "deleted_by", length = 50)
    private String deletedBy;

    public Instant getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Instant deletionDate) {
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