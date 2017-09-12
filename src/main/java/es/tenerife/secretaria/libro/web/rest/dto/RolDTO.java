package es.tenerife.secretaria.libro.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RolDTO extends AbstractVersionedEntityDTO implements Serializable {

	private static final long serialVersionUID = 3662329403989469628L;

	private Long id;

	private String nombre;

	private Set<OperacionDTO> operaciones = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<OperacionDTO> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(Set<OperacionDTO> operaciones) {
		this.operaciones = operaciones;
	}

}
