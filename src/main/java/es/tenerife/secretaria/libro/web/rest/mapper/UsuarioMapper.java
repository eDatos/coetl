package es.tenerife.secretaria.libro.web.rest.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;
import es.tenerife.secretaria.libro.web.rest.vm.ManagedUserVM;

@Service
public class UsuarioMapper {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	RolMapper rolMapper;

	public UsuarioDTO userToUserDTO(Usuario user) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(user.getId());
		usuarioDTO.setLogin(user.getLogin());
		usuarioDTO.setNombre(user.getNombre());
		usuarioDTO.setApellido1(user.getApellido1());
		usuarioDTO.setApellido2(user.getApellido2());
		usuarioDTO.setEmail(user.getEmail());
		usuarioDTO.setActivado(user.getActivado());
		usuarioDTO.setCreatedBy(user.getCreatedBy());
		usuarioDTO.setCreatedDate(user.getCreatedDate());
		usuarioDTO.setLastModifiedBy(user.getLastModifiedBy());
		usuarioDTO.setLastModifiedDate(user.getLastModifiedDate());
		usuarioDTO.setRoles(user.getRoles().stream().map(rolMapper::toDto).collect(Collectors.toSet()));
		usuarioDTO.setDeletionDate(user.getDeletionDate());
		usuarioDTO.setOptLock(user.getOptLock());
		return usuarioDTO;
	}

	public List<UsuarioDTO> usersToUserDTOs(List<Usuario> users) {
		return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
	}

	public Usuario userDTOToUser(UsuarioDTO userDTO) {
		if (userDTO == null) {
			return null;
		} else {
			Usuario user = new Usuario();
			user.setId(userDTO.getId());
			user.setLogin(userDTO.getLogin());
			user.setNombre(userDTO.getNombre());
			user.setApellido1(userDTO.getApellido1());
			user.setApellido2(userDTO.getApellido2());
			user.setEmail(userDTO.getEmail());
			user.setActivado(userDTO.isActivado());
			user.setDeletionDate(userDTO.getDeletionDate());
			user.setOptLock(userDTO.getOptLock());
			user.setRoles(rolMapper.toEntity(userDTO.getRoles()));
			return user;
		}
	}

	public List<Usuario> userDTOsToUsers(List<UsuarioDTO> userDTOs) {
		return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
	}

	public Usuario userFromId(Long id) {
		if (id == null) {
			return null;
		}
		return usuarioRepository.findOne(id);
	}

	public Usuario updateFromDTO(Usuario user, ManagedUserVM userVM) {
		if (user == null) {
			return null;
		}
		user.setLogin(userVM.getLogin());
		user.setNombre(userVM.getNombre());
		user.setApellido1(userVM.getApellido1());
		user.setApellido2(userVM.getApellido2());
		user.setEmail(userVM.getEmail());
		user.setActivado(userVM.isActivado());
		user.setOptLock(userVM.getOptLock());
		user.setRoles(rolMapper.toEntity(userVM.getRoles()));
		return user;
	}

	public UsuarioDTO usuarioLdapEntryToUsuarioDTO(UsuarioLdapEntry usuarioLdapEntry) {
		if (usuarioLdapEntry == null) {
			return null;
		}
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setLogin(usuarioLdapEntry.getNombreUsuario());
		usuarioDTO.setNombre(usuarioLdapEntry.getNombre());
		if (usuarioLdapEntry.getApellido1() != null) {
			usuarioDTO.setApellido1(usuarioLdapEntry.getApellido1());
			usuarioDTO.setApellido2(usuarioLdapEntry.getApellido2());
		} else {
			usuarioDTO.setApellido1(usuarioLdapEntry.getApellidos());
		}
		usuarioDTO.setEmail(usuarioLdapEntry.getCorreoElectronico());
		return usuarioDTO;
	}

}
