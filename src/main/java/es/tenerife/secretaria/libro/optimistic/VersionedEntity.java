package es.tenerife.secretaria.libro.optimistic;

public interface VersionedEntity {

	Long getId();

	Long getOptLock();

	void setOptLock(Long optLock);

}