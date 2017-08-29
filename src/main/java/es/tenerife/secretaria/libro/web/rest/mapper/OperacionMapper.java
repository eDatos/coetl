package es.tenerife.secretaria.libro.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.repository.OperacionRepository;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.web.rest.dto.OperacionDTO;

/**
 * Mapper for the entity Operacion and its DTO OperacionDTO.
 */
@Mapper(componentModel = "spring", uses = { RolMapper.class })
public abstract class OperacionMapper implements EntityMapper<OperacionDTO, Operacion> {

	@Autowired
	OperacionRepository operacionRepository;

	@Autowired
	RolRepository authorityRepository;

	public Operacion fromId(Long id) {
		if (id == null) {
			return null;
		}
		return operacionRepository.findOne(id);
	}

}
