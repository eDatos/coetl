package es.tenerife.secretaria.libro.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.SecretariaLibroApp;
import es.tenerife.secretaria.libro.repository.UserRepository;
import es.tenerife.secretaria.libro.security.jwt.TokenProvider;
import es.tenerife.secretaria.libro.web.rest.vm.LoginVM;

/**
 * Test class for the UserJWTController REST controller.
 *
 * @see UserJWTController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class UserJWTControllerIntTest {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		UserJWTController userJWTController = new UserJWTController(tokenProvider, authenticationManager);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userJWTController).build();
	}

	@Test
	@Transactional
	public void testAuthorizeFails() throws Exception {
		LoginVM login = new LoginVM();
		login.setUsername("wrong-user");
		mockMvc.perform(post("/api/authenticate").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(login))).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.id_token").doesNotExist());
	}
}
