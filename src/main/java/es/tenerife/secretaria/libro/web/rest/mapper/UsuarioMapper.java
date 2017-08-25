package es.tenerife.secretaria.libro.web.rest.mapper;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
			user.setApellidos(userDTO.getApellidos());
			user.setEmail(userDTO.getEmail());
			user.seturlImagen(userDTO.getUrlImagen());
			user.setActivado(userDTO.isActivo());
			user.setIdioma(userDTO.getIdioma());
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
		return strings.stream().map(string -> {
			Rol auth = new Rol();
			auth.setNombre(string);
			return auth;
		}).collect(Collectors.toSet());
	}
}
