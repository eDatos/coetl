package es.tenerife.secretaria.libro.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Operacion entity.
 */
public class OperacionDTO implements Serializable {

	private static final long serialVersionUID = 5895492694533589512L;

	private Long id;

	@NotNull
	private String accion;

	@NotNull
	private String sujeto;

	private Long optLock;

	private Set<RolDTO> roles = new HashSet<>();

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

	public Set<RolDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RolDTO> roles) {
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
