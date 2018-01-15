package com.arte.application.template.web.rest.mapper.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.application.template.domain.Operacion;
import com.arte.application.template.domain.Rol;
import com.arte.application.template.web.rest.dto.OperacionDTO;
import com.arte.application.template.web.rest.dto.RolDTO;
import com.arte.application.template.web.rest.mapper.OperacionMapper;
import com.arte.application.template.web.rest.mapper.RolMapper;

@Component
public class RolMapperImpl extends RolMapper {

	@Autowired
	OperacionMapper operacionMapper;

	@Override
	public Rol update(Rol entity, RolDTO dto) {

		if (dto == null) {
			return null;
		}

		entity.setOptLock(dto.getOptLock());
		entity.setNombre(dto.getNombre());
		entity.setCodigo(dto.getCodigo());
		entity.setId(dto.getId());
		if (entity.getOperaciones() != null) {
			Set<Operacion> set = operacionDTOSetToOperacionSet(dto.getOperaciones());
			if (set != null) {
				entity.getOperaciones().clear();
				entity.getOperaciones().addAll(set);
			} else {
				entity.setOperaciones(null);
			}
		} else {
			Set<Operacion> set = operacionDTOSetToOperacionSet(dto.getOperaciones());
			if (set != null) {
				entity.setOperaciones(set);
			}
		}

		return entity;
	}

	@Override
	public List<Rol> toEntity(List<RolDTO> dtoList) {
		if (dtoList == null) {
			return new ArrayList<>();
		}

		List<Rol> list = new ArrayList<>();
		for (RolDTO rolDTO : dtoList) {
			list.add(toEntity(rolDTO));
		}

		return list;
	}

	public Set<Rol> toEntity(Set<RolDTO> dtoList) {
		if (dtoList == null) {
			return new HashSet<>();
		}

		Set<Rol> set = new HashSet<>();
		for (RolDTO rolDTO : dtoList) {
			set.add(toEntity(rolDTO));
		}

		return set;
	}

	@Override
	public List<RolDTO> toDto(List<Rol> entityList) {
		if (entityList == null) {
			return new ArrayList<>();
		}

		List<RolDTO> list = new ArrayList<>();
		for (Rol rol : entityList) {
			list.add(toDto(rol));
		}

		return list;
	}

	public Set<RolDTO> toDto(Set<Rol> entityList) {
		if (entityList == null) {
			return new HashSet<>();
		}

		Set<RolDTO> set = new HashSet<>();
		for (Rol rol : entityList) {
			set.add(toDto(rol));
		}

		return set;
	}

	@Override
	public RolDTO toDto(Rol entity) {
		if (entity == null) {
			return null;
		}

		RolDTO rolDTO = new RolDTO();

		rolDTO.setId(entity.getId());
		rolDTO.setOptLock(entity.getOptLock());
		rolDTO.setNombre(entity.getNombre());
		rolDTO.setCodigo(entity.getCodigo());
		Set<OperacionDTO> set = operacionSetToOperacionDTOSet(entity.getOperaciones());
		if (set != null) {
			rolDTO.setOperaciones(set);
		}

		return rolDTO;
	}

	@Override
	public Rol toEntity(RolDTO dto) {

		if (dto == null || dto.getNombre() == null) {
			return null;
		}

		Rol rol = rolRepository.findOneByCodigo(dto.getCodigo());
		if (rol == null) {
			rol = new Rol();
			rol.setCodigo(dto.getCodigo());
			rol.setNombre(dto.getNombre());
			rol.setOptLock(dto.getOptLock());
			rol.setNombre(dto.getNombre());
			Set<Operacion> set = operacionDTOSetToOperacionSet(dto.getOperaciones());
			if (set != null) {
				rol.setOperaciones(set);
			}
		}

		return rol;
	}

	protected Set<Operacion> operacionDTOSetToOperacionSet(Set<OperacionDTO> setDTOs) {
		if (setDTOs == null) {
			return new HashSet<>();
		}

		Set<Operacion> setEntities = new HashSet<>();
		for (OperacionDTO operacionDTO : setDTOs) {
			setEntities.add(operacionMapper.fromId(operacionDTO.getId()));
		}

		return setEntities;
	}

	protected Set<OperacionDTO> operacionSetToOperacionDTOSet(Set<Operacion> setEntities) {
		if (setEntities == null) {
			return new HashSet<>();
		}

		Set<OperacionDTO> setDTOs = new HashSet<>();
		for (Operacion operacion : setEntities) {
			OperacionDTO operacionDTO = new OperacionDTO();
			operacionDTO.setAccion(operacion.getAccion());
			operacionDTO.setId(operacion.getId());
			operacionDTO.setSujeto(operacion.getSujeto());
			setDTOs.add(operacionDTO);
		}

		return setDTOs;
	}

}
