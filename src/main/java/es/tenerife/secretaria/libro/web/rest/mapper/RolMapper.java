package es.tenerife.secretaria.libro.web.rest.mapper;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;

public abstract class RolMapper implements EntityMapper<RolDTO, Rol> {

	@Autowired
	protected RolRepository rolRepository;

	public abstract Set<RolDTO> toDto(Set<Rol> entityList);

	public abstract Set<Rol> toEntity(Set<RolDTO> dtoList);

	public Rol fromNombre(String name) {
		if (name == null) {
			return null;
		}
		return rolRepository.findOneByCodigo(name);
	}

	public String nombreFromRol(Rol rol) {
		if (rol == null) {
			return null;
		}
		return rol.getNombre();
	}

}
