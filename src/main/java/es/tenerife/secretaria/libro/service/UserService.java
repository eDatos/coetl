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
import es.tenerife.secretaria.libro.domain.Authority;
import es.tenerife.secretaria.libro.domain.User;
import es.tenerife.secretaria.libro.repository.AuthorityRepository;
import es.tenerife.secretaria.libro.repository.UserRepository;
import es.tenerife.secretaria.libro.security.AuthoritiesConstants;
import es.tenerife.secretaria.libro.security.SecurityUtils;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	private final AuthorityRepository authorityRepository;

	public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}

	public User createUser(String login, String firstName, String lastName, String email, String imageUrl,
			String langKey) {

		User newUser = new User();
		Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
		Set<Authority> authorities = new HashSet<>();
		newUser.setLogin(login);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setImageUrl(imageUrl);
		newUser.setLangKey(langKey);
		// new user is not active
		newUser.setActivated(false);
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public User createUser(User user) {
		User newUser = new User();
		newUser.setLogin(user.getLogin());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setEmail(user.getEmail());
		newUser.setImageUrl(user.getImageUrl());
		if (user.getLangKey() == null) {
			newUser.setLangKey("en"); // default language
		} else {
			newUser.setLangKey(user.getLangKey());
		}
		if (user.getAuthorities() != null) {
			Set<Authority> authorities = new HashSet<>();
			user.getAuthorities()
					.forEach(authority -> authorities.add(authorityRepository.findOne(authority.getName())));
			newUser.setAuthorities(authorities);
		}
		newUser.setActivated(true);
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
	public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setLangKey(langKey);
			user.setImageUrl(imageUrl);
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
	public Optional<User> updateUser(User userDTO) {
		return Optional.of(userRepository.findOne(userDTO.getId())).map(user -> {
			user.setLogin(userDTO.getLogin());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmail(userDTO.getEmail());
			user.setImageUrl(userDTO.getImageUrl());
			user.setActivated(userDTO.getActivated());
			user.setLangKey(userDTO.getLangKey());
			Set<Authority> managedAuthorities = user.getAuthorities();
			managedAuthorities.clear();
			userDTO.getAuthorities().stream().map(Authority::getName).map(authorityRepository::findOne)
					.forEach(managedAuthorities::add);
			log.debug("Changed Information for User: {}", user);
			return user;
		});
	}

	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}

	@Transactional(readOnly = true)
	public Page<User> getAllManagedUsers(Pageable pageable) {
		return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities(Long id) {
		return userRepository.findOneWithAuthoritiesById(id);
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		List<User> users = userRepository
				.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}

	/**
	 * @return a list of all the authorities
	 */
	public List<String> getAuthorities() {
		return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
	}
}
