package es.tenerife.secretaria.libro.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import es.tenerife.secretaria.libro.service.criteria.UsuarioCriteriaProcessor;
import es.tenerife.secretaria.libro.service.criteria.UsuarioCriteriaProcessor.QueryProperty;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.UsuarioMapper;
import es.tenerife.secretaria.libro.web.rest.util.HeaderUtil;
import es.tenerife.secretaria.libro.web.rest.util.PaginationUtil;
import es.tenerife.secretaria.libro.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of
 * authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no View Model and DTO, a lot less code, and an outer-join which
 * would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 */
@RestController
@RequestMapping("/api")
public class UsuarioResource extends AbstractResource {

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

	/**
	 * POST /usuarios : Creates a new user.
	 * <p>
	 * Creates a new user if the login and email are not already used, and sends an
	 * mail with an activation link. The user needs to be activated on creation.
	 *
	 * @param managedUserVM
	 *            the user to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         user, or with status 400 (Bad Request) if the login or email is
	 *         already in use
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/usuarios")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'CREAR')")
	public ResponseEntity createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
		log.debug("REST request to save User : {}", managedUserVM);

		if (managedUserVM.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
					.body(null);
			// Lowercase the user login before comparing with database
		} else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
					.body(null);
		} else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
					.body(null);
		} else {
			Usuario newUser = usuarioService.createUsuario(usuarioMapper.userDTOToUser(managedUserVM));
			mailService.sendCreationEmail(newUser);
			auditPublisher.publish(AuditConstants.USUARIO_CREACION, managedUserVM.getLogin());
			return ResponseEntity.created(new URI("/api/usuarios/" + newUser.getLogin()))
					.headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin())).body(newUser);
		}
	}

	/**
	 * PUT /usuarios : Updates an existing User.
	 *
	 * @param managedUserVM
	 *            the user to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         user, or with status 400 (Bad Request) if the login or email is
	 *         already in use, or with status 500 (Internal Server Error) if the
	 *         user couldn't be updated
	 */
	@PutMapping("/usuarios")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'EDITAR')")
	public ResponseEntity<UsuarioDTO> updateUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
		log.debug("REST request to update User : {}", managedUserVM);
		Optional<Usuario> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
					.body(null);
		}
		if (!existingUser.isPresent()) {
			existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
		}
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
					.body(null);
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

	/**
	 * GET /usuarios : get all users.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and with body all users
	 */
	@GetMapping("/usuarios")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'LEER')")
	public ResponseEntity<List<UsuarioDTO>> getAllUsers(@ApiParam Pageable pageable,
			@ApiParam(defaultValue = "false") Boolean includeDeleted, @ApiParam(required = false) String query) {
		String queryString = buildUserQueryFromSearch(query);
		final Page<UsuarioDTO> page = usuarioService.getAllUsuarios(pageable, includeDeleted, queryString)
				.map(usuarioMapper::userToUserDTO);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usuarios");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /usuarios/:login : get the "login" user.
	 *
	 * @param login
	 *            the login of the user to find
	 * @return the ResponseEntity with status 200 (OK) and with body the "login"
	 *         user, or with status 404 (Not Found)
	 */
	@GetMapping("/usuarios/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'LEER')")
	public ResponseEntity<UsuarioDTO> getUser(@PathVariable String login,
			@ApiParam(required = false, defaultValue = "false") Boolean includeDeleted) {
		log.debug("REST request to get User : {}", login);
		return ResponseUtil.wrapOrNotFound(usuarioService.getUsuarioWithAuthoritiesByLogin(login, includeDeleted)
				.map(usuarioMapper::userToUserDTO));
	}

	@GetMapping("/usuarios/{login:" + Constants.LOGIN_REGEX + "}/ldap")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'LEER')")
	public ResponseEntity<UsuarioDTO> getUserFromLdap(@PathVariable String login) {
		log.debug("REST request to get User from LDAP : {}", login);
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

	/**
	 * DELETE /usuarios/:login : delete the "login" User.
	 *
	 * @param login
	 *            the login of the user to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/usuarios/{login:" + Constants.LOGIN_REGEX + "}")
	@Timed
	@PreAuthorize("hasPermission('USUARIO', 'BORRAR')")
	public ResponseEntity<Void> deleteUser(@PathVariable String login) {
		log.debug("REST request to delete User: {}", login);
		usuarioService.deleteUsuario(login);
		auditPublisher.publish(AuditConstants.USUARIO_DESACTIVACION, login);
		return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
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
		return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createAlert("userManagement.updated", login));
	}

	private String buildUserQueryFromSearch(String userSearch) {
		StringBuilder queryBuilder = new StringBuilder();
		if (StringUtils.isNotBlank(userSearch)) {
			//@formatter:off
			List<QueryProperty> queryProperties = Arrays.asList(UsuarioCriteriaProcessor.QueryProperty.values());
			for (QueryProperty property : queryProperties ) {
				queryBuilder = queryBuilder.append(property).append(" ILIKE ")
						.append("'%").append(userSearch).append("%'");
				if (queryProperties.indexOf(property) != queryProperties.size() - 1 ) {
						queryBuilder.append(" OR ");
				}
			}
			//@formatter:on
		}
		return queryBuilder.toString();
	}
}
