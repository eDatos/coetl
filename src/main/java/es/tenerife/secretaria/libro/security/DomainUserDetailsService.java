package es.tenerife.secretaria.libro.security;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

	private final UsuarioRepository userRepository;

	public DomainUserDetailsService(UsuarioRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) {
		log.debug("Authenticating {}", login);
		String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
		Optional<Usuario> userFromDatabase = userRepository.findOneWithRolesByLogin(lowercaseLogin);
		return userFromDatabase.map(user -> {
			if (!user.getActivado()) {
				throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
			}
			List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
					.map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
			return new org.springframework.security.core.userdetails.User(lowercaseLogin, "", grantedAuthorities);
		}).orElseThrow(
				() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " + "database"));
	}
}
