package es.tenerife.secretaria.libro.web.rest.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.tenerife.secretaria.libro.domain.Rol;

public class OperacionDTO implements Serializable {

	private static final long serialVersionUID = 5895492694533589512L;

	private Long id;

	@NotNull
	private String accion;

	@NotNull
	private String sujeto;

	private Long optLock;

	private List<Rol> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getSujeto() {
		return sujeto;
	}

	public void setSujeto(String sujeto) {
		this.sujeto = sujeto;
	}

	public Long getOptLock() {
		return optLock;
	}

	public void setOptLock(Long optLock) {
		this.optLock = optLock;
	}

	@JsonProperty
	public List<Rol> getRoles() {
		return roles;
	}

	@JsonIgnore
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		OperacionDTO operacionDTO = (OperacionDTO) o;
		if (operacionDTO.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), operacionDTO.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "OperacionDTO{" + "id=" + getId() + ", accion='" + getAccion() + "'" + ", sujeto='" + getSujeto() + "'"
				+ "}";
	}
}
