package es.tenerife.secretaria.libro.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonView;

public abstract class AbstractVersionedEntityDTO {

	@JsonView(Views.Minimal.class)
	private Long optLock;

	public Long getOptLock() {
		return optLock;
	}

	public void setOptLock(Long optLock) {
		this.optLock = optLock;
	}
}