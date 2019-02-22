package es.gobcan.coetl.domain;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import es.gobcan.coetl.optimistic.OptimisticLockChecker;

@MappedSuperclass
@EntityListeners(OptimisticLockChecker.class)
public abstract class AbstractVersionedAndAuditingEntity extends AbstractAuditingEntity implements VersionedEntity {

    private static final long serialVersionUID = -3342374611675636866L;

    @Version
    @Column(name = "opt_lock")
    private Long optLock;

    @Override
    public Long getOptLock() {
        return optLock;
    }

    @Override
    public void setOptLock(Long optLock) {
        this.optLock = optLock;
    }
}