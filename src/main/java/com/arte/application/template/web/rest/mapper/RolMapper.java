package com.arte.application.template.web.rest.mapper;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.arte.application.template.domain.Rol;
import com.arte.application.template.repository.RolRepository;
import com.arte.application.template.web.rest.dto.RolDTO;

public abstract class RolMapper implements EntityMapper<RolDTO, Rol> {

	@Autowired
	protected RolRepository rolRepository;

	public abstract Set<RolDTO> toDto(Set<Rol> entityList);

	public abstract Set<Rol> toEntity(Set<RolDTO> dtoList);

	public Rol fromCodigo(String codigo) {
		if (codigo == null) {
			return null;
		}
		return rolRepository.findOneByCodigo(codigo);
	}

}
