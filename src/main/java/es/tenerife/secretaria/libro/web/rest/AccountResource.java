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

import es.tenerife.secretaria.libro.domain.User;
import es.tenerife.secretaria.libro.repository.UserRepository;
import es.tenerife.secretaria.libro.security.SecurityUtils;
import es.tenerife.secretaria.libro.service.UserService;
import es.tenerife.secretaria.libro.web.rest.dto.UserDTO;
import es.tenerife.secretaria.libro.web.rest.util.HeaderUtil;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource extends AbstractResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserRepository userRepository;

	private final UserService userService;

	public AccountResource(UserRepository userRepository, UserService userService) {

		this.userRepository = userRepository;
		this.userService = userService;
	}

	/**
	 * GET /authenticate : check if the user is authenticated, and return its login.
	 *
	 * @param request
	 *            the HTTP request
	 * @return the login if the user is authenticated
	 */
	@GetMapping("/authenticate")
	@Timed
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	/**
	 * GET /account : get the current user.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the current user in body,
	 *         or status 500 (Internal Server Error) if the user couldn't be
	 *         returned
	 */
	@GetMapping("/account")
	@Timed
	public ResponseEntity<UserDTO> getAccount() {
		return Optional.ofNullable(userService.getUserWithAuthorities())
				.map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * POST /account : update the current user information.
	 *
	 * @param userDTO
	 *            the current user information
	 * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request)
	 *         or 500 (Internal Server Error) if the user couldn't be updated
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/account")
	@Timed
	public ResponseEntity saveAccount(@Valid @RequestBody UserDTO userDTO) {
		final String userLogin = SecurityUtils.getCurrentUserLogin();
		Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
					.body(null);
		}
		return userRepository.findOneByLogin(userLogin).map(u -> {
			userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
					userDTO.getLangKey(), userDTO.getImageUrl());
			return new ResponseEntity(HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
