package es.gobcan.coetl.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base abstract class for entities which will hold definitions for created,
 * last modified by and created, last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingWithDeletionEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = -4785510038403987259L;

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
        return deletionDate == null;
    }

}
