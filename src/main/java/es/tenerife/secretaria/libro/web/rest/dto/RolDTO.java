package es.tenerife.secretaria.libro.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

public class RolDTO extends AbstractVersionedEntityDTO implements Serializable {

	private static final long serialVersionUID = 3662329403989469628L;

	@JsonView(Views.Minimal.class)
	private Long id;

	@JsonView(Views.Minimal.class)
	private String codigo;

	@JsonView(Views.Minimal.class)
	private String nombre;

	private Set<OperacionDTO> operaciones = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
