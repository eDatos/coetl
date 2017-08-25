package es.tenerife.secretaria.libro.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import es.tenerife.secretaria.libro.repository.AuthorityRepository;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.security.AuthoritiesConstants;
import es.tenerife.secretaria.libro.security.SecurityUtils;

/**
 * Service class for managing users.
 */
@Service
public class UsuarioService {

	private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

	private final UsuarioRepository userRepository;

	private final AuthorityRepository authorityRepository;

	public UsuarioService(UsuarioRepository userRepository, AuthorityRepository authorityRepository) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}

	public Usuario createUsuario(String login, String firstName, String lastName, String email, String imageUrl,
			String langKey) {

		Usuario newUser = new Usuario();
		Rol authority = authorityRepository.findOne(AuthoritiesConstants.USER);
		Set<Rol> authorities = new HashSet<>();
		newUser.setLogin(login);
		newUser.setNombre(firstName);
		newUser.setApellidos(lastName);
		newUser.setEmail(email);
		newUser.seturlImagen(imageUrl);
		newUser.setIdioma(langKey);
		// new user is not active
		newUser.setActivado(false);
		authorities.add(authority);
		newUser.setRoles(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public Usuario createUsuario(Usuario user) {
		Usuario newUser = new Usuario();
		newUser.setLogin(user.getLogin());
		newUser.setNombre(user.getNombre());
		newUser.setApellidos(user.getApellidos());
		newUser.setEmail(user.getEmail());
		newUser.seturlImagen(user.getUrlImagen());
		if (user.getIdioma() == null) {
			newUser.setIdioma("en"); // default language
		} else {
			newUser.setIdioma(user.getIdioma());
		}
		if (user.getRoles() != null) {
			Set<Rol> authorities = new HashSet<>();
			user.getRoles()
					.forEach(authority -> authorities.add(authorityRepository.findOne(authority.getName())));
			newUser.setRoles(authorities);
		}
		newUser.setActivado(true);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	/**
	 * Update basic information (first name, last name, email, language) for the
	 * current user.
	 *
	 * @param firstName
	 *            first name of user
	 * @param lastName
	 *            last name of user
	 * @param email
	 *            email id of user
	 * @param langKey
	 *            language key
	 * @param imageUrl
	 *            image URL of user
	 */
	public void updateUsuario(String firstName, String lastName, String email, String langKey, String imageUrl) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			user.setNombre(firstName);
			user.setApellidos(lastName);
			user.setEmail(email);
			user.setIdioma(langKey);
			user.seturlImagen(imageUrl);
			log.debug("Changed Information for User: {}", user);
		});
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param userDTO
	 *            user to update
	 * @return updated user
	 */
	public Optional<Usuario> updateUsuario(Usuario userDTO) {
		return Optional.of(userRepository.findOne(userDTO.getId())).map(user -> {
			user.setLogin(userDTO.getLogin());
			user.setNombre(userDTO.getNombre());
			user.setApellidos(userDTO.getApellidos());
			user.setEmail(userDTO.getEmail());
			user.seturlImagen(userDTO.getUrlImagen());
			user.setActivado(userDTO.getActivado());
			user.setIdioma(userDTO.getIdioma());
			Set<Rol> managedAuthorities = user.getRoles();
			managedAuthorities.clear();
			userDTO.getRoles().stream().map(Rol::getName).map(authorityRepository::findOne)
					.forEach(managedAuthorities::add);
			log.debug("Changed Information for User: {}", user);
			return user;
		});
	}

	public void deleteUsuario(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}

	@Transactional(readOnly = true)
	public Page<Usuario> getAllUsuarios(Pageable pageable) {
		return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER);
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> getUsuarioWithAuthoritiesByLogin(String login) {
		return userRepository.findOneWithRolesByLogin(login);
	}

	@Transactional(readOnly = true)
	public Usuario getUsuarioWithAuthorities(Long id) {
		return userRepository.findOneWithRolesById(id);
	}

	@Transactional(readOnly = true)
	public Usuario getUsuarioWithAuthorities() {
		return userRepository.findOneWithRolesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeUsuariosNoActivados() {
		List<Usuario> users = userRepository
				.findAllByActivadoIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
		for (Usuario user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}

	/**
	 * @return a list of all the authorities
	 */
	public List<String> getAuthorities() {
		return authorityRepository.findAll().stream().map(Rol::getName).collect(Collectors.toList());
	}
}
