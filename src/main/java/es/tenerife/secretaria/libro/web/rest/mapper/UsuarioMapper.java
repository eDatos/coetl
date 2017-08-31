package es.tenerife.secretaria.libro.web.rest.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;

/**
 * Mapper for the entity User and its DTO called UserDTO.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as
 * MapStruct support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UsuarioMapper {

	public UsuarioDTO userToUserDTO(Usuario user) {
		return new UsuarioDTO(user);
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
			user.seturlImagen(userDTO.getUrlImagen());
			user.setActivado(userDTO.isActivado());
			user.setIdioma(userDTO.getIdioma());
			user.setDeletionDate(userDTO.getDeletionDate());
			Set<Rol> authorities = this.authoritiesFromStrings(userDTO.getRoles());
			if (authorities != null) {
				user.setRoles(authorities);
			}
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
		Usuario user = new Usuario();
		user.setId(id);
		return user;
	}

	public Set<Rol> authoritiesFromStrings(Set<String> strings) {
		if (strings == null) {
			return new HashSet<>();
		}
		return strings.stream().map(string -> {
			Rol auth = new Rol();
			auth.setNombre(string);
			return auth;
		}).collect(Collectors.toSet());
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
