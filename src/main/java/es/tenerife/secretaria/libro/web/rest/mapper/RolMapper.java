package es.tenerife.secretaria.libro.web.rest.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;

public abstract class RolMapper implements EntityMapper<RolDTO, Rol> {

	@Autowired
	protected RolRepository rolRepository;

	public Rol fromName(String name) {
		if (name == null) {
			return null;
		}
		return rolRepository.findOne(name);
	}

}
