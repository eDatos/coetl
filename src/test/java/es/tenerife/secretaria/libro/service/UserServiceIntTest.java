package es.tenerife.secretaria.libro.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
@Transactional
public class UserServiceIntTest {

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private UsuarioService userService;

	@Test
	public void assertThatAnonymousUserIsNotGet() {
		final PageRequest pageable = new PageRequest(0, (int) userRepository.count());
		final Page<Usuario> allManagedUsers = userService.getAllUsuarios(pageable, false, null);
		assertThat(allManagedUsers.getContent().stream()
				.noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin()))).isTrue();
	}

}
