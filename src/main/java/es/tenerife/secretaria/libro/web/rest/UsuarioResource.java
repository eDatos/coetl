package es.tenerife.secretaria.libro.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.tenerife.secretaria.libro.config.AuditConstants;
import es.tenerife.secretaria.libro.config.Constants;
import es.tenerife.secretaria.libro.config.audit.AuditEventPublisher;
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.service.LdapService;
import es.tenerife.secretaria.libro.service.MailService;
import es.tenerife.secretaria.libro.service.UsuarioService;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;
import es.tenerife.secretaria.libro.web.rest.errors.ErrorConstants;
import es.tenerife.secretaria.libro.web.rest.mapper.UsuarioMapper;
import es.tenerife.secretaria.libro.web.rest.util.HeaderUtil;
import es.tenerife.secretaria.libro.web.rest.util.PaginationUtil;
import es.tenerife.secretaria.libro.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class UsuarioResource extends AbstractResource {

	private static final String EMAIL_EXISTS = "emailexists";

	private static final String USER_EXISTS = "userexists";

	private final Logger log = LoggerFactory.getLogger(UsuarioResource.class);

	private static final String ENTITY_NAME = "userManagement";

	private final UsuarioRepository userRepository;

	private final MailService mailService;

	private final UsuarioService usuarioService;

	private UsuarioMapper usuarioMapper;

	private LdapService ldapService;

	private AuditEventPublisher auditPublisher;

	public UsuarioResource(UsuarioRepository userRepository, MailService mailService, UsuarioService userService,
			UsuarioMapper userMapper, LdapService ldapService, AuditEventPublisher auditPublisher) {

		this.userRepository = userRepository;
		this.mailService = mailService;
		this.usuarioService = userService;
		this.usuarioMapper = userMapper;
		this.ldapService = ldapService;
		this.auditPublisher = auditPublisher;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/usuarios")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'CREAR')")
	public ResponseEntity createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
		log.debug("REST Petición para guardar User : {}", managedUserVM);

		if (managedUserVM.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
					ErrorConstants.ID_EXISTE, "Un usuario no puede tener ID")).body(null);
		} else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, USER_EXISTS, "Login ya en uso")).body(null);
		} else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, EMAIL_EXISTS, "Email ya en uso")).body(null);
		} else {
			Usuario newUser = usuarioService.createUsuario(usuarioMapper.userDTOToUser(managedUserVM));
			mailService.sendCreationEmail(newUser);
			auditPublisher.publish(AuditConstants.USUARIO_CREACION, managedUserVM.getLogin());
			return ResponseEntity.created(new URI("/api/usuarios/" + newUser.getLogin()))
					.headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin())).body(newUser);
		}
	}

	@PutMapping("/usuarios")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'EDITAR')")
	public ResponseEntity<UsuarioDTO> updateUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
		log.debug("REST petición para actualizar User : {}", managedUserVM);
		Optional<Usuario> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, EMAIL_EXISTS, "Email ya en uso")).body(null);
		}
		if (!existingUser.isPresent()) {
			existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
		}
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, USER_EXISTS, "Login ya en uso")).body(null);
		}
		if (!existingUser.isPresent()) {
			existingUser = Optional.ofNullable(userRepository.findOne(managedUserVM.getId()));
		}
		Usuario usuario = usuarioMapper.updateFromDTO(existingUser.orElse(null), managedUserVM);
		usuario = usuarioService.updateUsuario(usuario);
		Optional<UsuarioDTO> updatedUser = Optional.ofNullable(usuarioMapper.userToUserDTO(usuario));

		auditPublisher.publish(AuditConstants.USUARIO_EDICION, managedUserVM.getLogin());
		return ResponseUtil.wrapOrNotFound(updatedUser,
				HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()));
	}

	@GetMapping("/usuarios")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'LEER')")
	public ResponseEntity<List<UsuarioDTO>> getAllUsers(@ApiParam Pageable pageable,
			@ApiParam(defaultValue = "false") Boolean includeDeleted, @ApiParam(required = false) String query) {
		final Page<UsuarioDTO> page = usuarioService.getAllUsuarios(pageable, includeDeleted, query)
				.map(usuarioMapper::userToUserDTO);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usuarios");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/usuarios/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'LEER')")
	public ResponseEntity<UsuarioDTO> getUser(@PathVariable String login,
			@ApiParam(required = false, defaultValue = "false") Boolean includeDeleted) {
		log.debug("REST petición para obtener  User : {}", login);
		return ResponseUtil.wrapOrNotFound(usuarioService.getUsuarioWithAuthoritiesByLogin(login, includeDeleted)
				.map(usuarioMapper::userToUserDTO));
	}

	@GetMapping("/usuarios/{login:" + Constants.LOGIN_REGEX + "}/ldap")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'LEER')")
	public ResponseEntity<UsuarioDTO> getUserFromLdap(@PathVariable String login) {
		log.debug("REST petición para obtener  User from LDAP : {}", login);
		UsuarioLdapEntry usuarioLdap = ldapService.buscarUsuarioLdap(login);
		if (usuarioLdap == null) {
			return ResponseEntity
					.notFound().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
							"userManagement.usuario-ldap-no-encontrado", "No se ha encontrado el usuario LDAP"))
					.build();
		}
		UsuarioDTO usuarioDTO = usuarioMapper.usuarioLdapEntryToUsuarioDTO(usuarioLdap);
		return ResponseEntity.ok().body(usuarioDTO);
	}

	@DeleteMapping("/usuarios/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'BORRAR')")
	public ResponseEntity<Void> deleteUser(@PathVariable String login) {
		log.debug("REST request to delete User: {}", login);
		usuarioService.deleteUsuario(login);
		auditPublisher.publish(AuditConstants.USUARIO_DESACTIVACION, login);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, login)).build();
	}

	@PutMapping("/usuarios/{login}/restore")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'EDITAR')")
	public ResponseEntity<UsuarioDTO> updateUser(@Valid @PathVariable String login) {
		log.debug("REST request to restore User : {}", login);

		Optional<Usuario> deletedUser = userRepository.findOneByLogin(login.toLowerCase());
		if (deletedUser.isPresent()) {
			usuarioService.restore(deletedUser.get());
		}

		Optional<UsuarioDTO> updatedUser = Optional.ofNullable(usuarioMapper.userToUserDTO(deletedUser.orElse(null)));

		auditPublisher.publish(AuditConstants.USUARIO_ACTIVACION, login);
		return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, login));
	}
}
