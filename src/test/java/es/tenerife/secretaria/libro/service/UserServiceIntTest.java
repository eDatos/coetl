package es.tenerife.secretaria.libro.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.SecretariaLibroApp;
import es.tenerife.secretaria.libro.config.Constants;
import es.tenerife.secretaria.libro.domain.User;
import es.tenerife.secretaria.libro.repository.UserRepository;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
@Transactional
public class UserServiceIntTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test

	public void testFindNotActivatedUsersByCreationDateBefore() {
		userService.removeNotActivatedUsers();
		Instant now = Instant.now();
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
		assertThat(users).isEmpty();
	}

	@Test
	public void assertThatAnonymousUserIsNotGet() {
		final PageRequest pageable = new PageRequest(0, (int) userRepository.count());
		final Page<User> allManagedUsers = userService.getAllManagedUsers(pageable);
		assertThat(allManagedUsers.getContent().stream()
				.noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
	}

	@Test
	public void testRemoveNotActivatedUsers() {
		User user = userService.createUser("johndoe", "John", "Doe", "john.doe@localhost", "http://placehold.it/50x50",
				"en-US");
		user.setActivated(false);
		user.setCreatedDate(Instant.now().minus(30, ChronoUnit.DAYS));
		userRepository.save(user);
		assertThat(userRepository.findOneByLogin("johndoe")).isPresent();
		userService.removeNotActivatedUsers();
		assertThat(userRepository.findOneByLogin("johndoe")).isNotPresent();
	}
}
