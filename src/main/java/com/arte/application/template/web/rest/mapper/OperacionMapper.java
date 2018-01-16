package com.arte.application.template.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.application.template.domain.Operacion;
import com.arte.application.template.repository.OperacionRepository;
import com.arte.application.template.repository.RolRepository;
import com.arte.application.template.web.rest.dto.OperacionDTO;

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
