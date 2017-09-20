package es.tenerife.secretaria.libro.web.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.security.SecurityUtils;
import es.tenerife.secretaria.libro.service.UsuarioService;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.UsuarioMapper;
import es.tenerife.secretaria.libro.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class AccountResource extends AbstractResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UsuarioRepository userRepository;

	private final UsuarioService userService;

	private UsuarioMapper usuarioMapper;

	public AccountResource(UsuarioRepository userRepository, UsuarioService userService, UsuarioMapper usuarioMapper) {

		this.userRepository = userRepository;
		this.userService = userService;
		this.usuarioMapper = usuarioMapper;
	}

	@GetMapping("/authenticate")
	@Timed
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST petición para comprobar si el usuario actual está autenticado");
		return request.getRemoteUser();
	}

	@GetMapping("/account")
	@Timed
	public ResponseEntity<UsuarioDTO> getAccount() {
		return Optional.ofNullable(userService.getUsuarioWithAuthorities())
				.map(user -> new ResponseEntity<>(usuarioMapper.userToUserDTO(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/account")
	@Timed
	public ResponseEntity saveAccount(@Valid @RequestBody UsuarioDTO userDTO) {
		final String userLogin = SecurityUtils.getCurrentUserLogin();
		Optional<Usuario> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
					.body(null);
		}
		return userRepository.findOneByLoginAndDeletionDateIsNull(userLogin).map(u -> {
			userService.updateUsuario(userDTO.getNombre(), userDTO.getApellido1(), userDTO.getApellido2(),
					userDTO.getEmail());
			return new ResponseEntity(HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
