package es.tenerife.secretaria.libro.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;

/**
 * Mapper for the entity Rol and its DTO RolDTO.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, OperacionMapper.class })
public abstract class RolMapper implements EntityMapper<RolDTO, Rol> {

	@Autowired
	RolRepository rolRepository;

	public Rol fromName(String name) {
		if (name == null) {
			return null;
		}
		return rolRepository.findOne(name);
	}

}
