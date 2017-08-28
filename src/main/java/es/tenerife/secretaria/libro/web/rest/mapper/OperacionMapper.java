package es.tenerife.secretaria.libro.web.rest.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.AuthorityRepository;
import es.tenerife.secretaria.libro.repository.OperacionRepository;
import es.tenerife.secretaria.libro.web.rest.dto.OperacionDTO;

/**
 * Mapper for the entity Operacion and its DTO OperacionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class OperacionMapper implements EntityMapper<OperacionDTO, Operacion> {

	@Autowired
	OperacionRepository operacionRepository;

	@Autowired
	AuthorityRepository authorityRepository;

	@Mapping(source = "roles", target = "roles")
	public abstract Operacion toEntity(OperacionDTO dto);

	@Mapping(source = "roles", target = "roles")
	public abstract OperacionDTO toDto(Operacion entity);

	public Operacion fromId(Long id) {
		if (id == null) {
			return null;
		}
		return operacionRepository.findOne(id);
	}

	Set<Rol> rolesFromString(Set<String> strings) {
		return strings.stream().map(authorityRepository::findOne).collect(Collectors.toSet());
	}

	Set<String> rolesToString(Set<Rol> roles) {
		return roles.stream().map(Rol::getName).collect(Collectors.toSet());
	}
}
