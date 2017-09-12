package es.tenerife.secretaria.libro.web.rest.mapper.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.web.rest.dto.OperacionDTO;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.OperacionMapper;
import es.tenerife.secretaria.libro.web.rest.mapper.RolMapper;

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

	@Override
	public RolDTO toDto(Rol entity) {
		if (entity == null) {
			return null;
		}

		RolDTO rolDTO = new RolDTO();

		rolDTO.setId(entity.getId());
		rolDTO.setOptLock(entity.getOptLock());
		rolDTO.setNombre(entity.getNombre());
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

		Rol rol = rolRepository.findOneByNombre(dto.getNombre());
		if (rol == null) {
			rol = new Rol();
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
			setEntities.add(operacionMapper.toEntity(operacionDTO));
		}

		return setEntities;
	}

	protected Set<OperacionDTO> operacionSetToOperacionDTOSet(Set<Operacion> setEntities) {
		if (setEntities == null) {
			return new HashSet<>();
		}

		Set<OperacionDTO> setDTOs = new HashSet<>();
		for (Operacion operacion : setEntities) {
			setDTOs.add(operacionMapper.toDto(operacion));
		}

		return setDTOs;
	}

}
