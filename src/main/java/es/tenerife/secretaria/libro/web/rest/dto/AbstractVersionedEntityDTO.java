package es.tenerife.secretaria.libro.web.rest.dto;

public abstract class AbstractVersionedEntityDTO {

	private Long optLock;

	public Long getOptLock() {
		return optLock;
	}

	public void setOptLock(Long optLock) {
		this.optLock = optLock;
	}
}