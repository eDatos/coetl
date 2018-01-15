package es.tenerife.secretaria.libro.optimistic;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
@EntityListeners(OptimisticLockChecker.class)
public abstract class AbstractVersionedEntity implements VersionedEntity {

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