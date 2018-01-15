package com.arte.application.template.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.ArteApplicationTemplateApp;
import com.arte.application.template.config.Constants;
import com.arte.application.template.domain.Usuario;
import com.arte.application.template.repository.UsuarioRepository;
import com.arte.application.template.service.UsuarioService;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
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
