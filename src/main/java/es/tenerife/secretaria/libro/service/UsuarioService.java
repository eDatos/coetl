package es.tenerife.secretaria.libro.service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.config.Constants;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.security.SecurityUtils;
import es.tenerife.secretaria.libro.web.rest.errors.CustomParameterizedException;
import es.tenerife.secretaria.libro.web.rest.util.QueryUtil;

/**
 * Service class for managing users.
 */
@Service
public class UsuarioService {

	private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

	private final UsuarioRepository usuarioRepository;

	private final RolRepository authorityRepository;

	private LdapService ldapService;

	private QueryUtil queryUtil;

	public UsuarioService(UsuarioRepository userRepository, RolRepository authorityRepository, LdapService ldapService,
			QueryUtil queryUtil) {
		this.usuarioRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.ldapService = ldapService;
		this.queryUtil = queryUtil;
	}

	public Usuario createUsuario(@NotNull Usuario user) {
		validarUsuarioLdap(user);
		Usuario newUser = new Usuario();
		newUser.setLogin(user.getLogin());
		newUser.setNombre(user.getNombre());
		newUser.setApellido1(user.getApellido1());
		newUser.setApellido2(user.getApellido2());
		newUser.setEmail(user.getEmail());
		newUser.seturlImagen(user.getUrlImagen());
		if (user.getRoles() != null) {
			Set<Rol> authorities = new HashSet<>();
			user.getRoles()
					.forEach(authority -> authorities.add(authorityRepository.findOneByCodigo(authority.getNombre())));
			newUser.setRoles(authorities);
		}
		newUser.setActivado(true);
		usuarioRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	/**
	 * Update basic information (first name, last name, email) for the
	 * current user.
	 *
	 * @param firstName
	 *            first name of user
	 * @param apellido1
	 *            last name of user
	 * @param email
	 *            email id of user
	 * @param imageUrl
	 *            image URL of user
	 */
	public void updateUsuario(String firstName, String apellido1, String apellido2, String email,
			String imageUrl) {
		usuarioRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			validarUsuarioLdap(user);
			user.setNombre(firstName);
			user.setApellido1(apellido1);
			user.setApellido2(apellido2);
			user.setEmail(email);
			user.seturlImagen(imageUrl);
			log.debug("Changed Information for User: {}", user);
		});
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param user
	 *            user to update
	 * @return updated user
	 */
	public Usuario updateUsuario(Usuario user) {
		validarUsuarioLdap(user);
		return usuarioRepository.save(user);
	}

	public void deleteUsuario(String login) {
		usuarioRepository.findOneByLoginAndDeletionDateIsNull(login).ifPresent(user -> {
			user.setDeletionDate(ZonedDateTime.now());
			usuarioRepository.save(user);
			log.debug("Deleted User: {}", user);
		});
	}

	public void restore(Usuario usuario) {
		if (usuario == null) {
			throw new CustomParameterizedException("error.userManagement.usuario-no-valido");
		}
		usuario.setDeletionDate(null);
		usuarioRepository.save(usuario);
		log.debug("Restored User: {}", usuario);
	}

	@Transactional(readOnly = true)
	public Page<Usuario> getAllUsuarios(Pageable pageable, Boolean includeDeleted, String query) {
		if (!StringUtils.isEmpty(query)) {
			String finalQuery = "(" + query + ") AND LOGIN NE '" + Constants.ANONYMOUS_USER + "'";
			if (BooleanUtils.isTrue(includeDeleted)) {
				finalQuery = queryUtil.queryIncludingDeleted(finalQuery);
			}
			DetachedCriteria criteria = queryUtil.queryToUserCriteria(finalQuery);
			return usuarioRepository.findAll(criteria, pageable);
		}
		if (BooleanUtils.isTrue(includeDeleted)) {
			return usuarioRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER);
		}
		return usuarioRepository.findAllByLoginNotAndDeletionDateIsNull(pageable, Constants.ANONYMOUS_USER);
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> getUsuarioWithAuthoritiesByLogin(String login, Boolean includeDeleted) {
		if (BooleanUtils.isTrue(includeDeleted)) {
			return usuarioRepository.findOneByLogin(login);
		} else {
			return usuarioRepository.findOneWithRolesByLoginAndDeletionDateIsNull(login);
		}
	}

	@Transactional(readOnly = true)
	public Usuario getUsuarioWithAuthorities(Long id) {
		return usuarioRepository.findOneWithRolesByIdAndDeletionDateIsNull(id);
	}

	@Transactional(readOnly = true)
	public Usuario getUsuarioWithAuthorities() {
		return usuarioRepository.findOneWithRolesByLoginAndDeletionDateIsNull(SecurityUtils.getCurrentUserLogin())
				.orElse(null);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeUsuariosNoActivados() {
		List<Usuario> users = usuarioRepository.findAllByActivadoIsFalseAndCreatedDateBeforeAndDeletionDateIsNull(
				Instant.now().minus(3, ChronoUnit.DAYS));
		for (Usuario user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			deleteUsuario(user.getLogin());
		}
	}

	private void validarUsuarioLdap(Usuario user) {
		if (ldapService.buscarUsuarioLdap(user.getLogin()) == null) {
			throw new CustomParameterizedException("error.userManagement.usuario-ldap-no-encontrado", user.getLogin());
		}
	}

}
