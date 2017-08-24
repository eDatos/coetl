package es.tenerife.secretaria.libro.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.SecretariaLibroApp;
import es.tenerife.secretaria.libro.domain.Authority;
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.security.AuthoritiesConstants;
import es.tenerife.secretaria.libro.service.MailService;
import es.tenerife.secretaria.libro.service.UsuarioService;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class AccountResourceIntTest {

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private UsuarioService userService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private HttpMessageConverter[] httpMessageConverters;

	@Mock
	private UsuarioService mockUserService;

	@Mock
	private MailService mockMailService;

	private MockMvc restUserMockMvc;

	private MockMvc restMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		doNothing().when(mockMailService).sendActivationEmail(anyObject());

		AccountResource accountResource = new AccountResource(userRepository, userService);

		AccountResource accountUserMockResource = new AccountResource(userRepository, mockUserService);

		this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).setMessageConverters(httpMessageConverters)
				.build();
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
	}

	@Test
	public void testNonAuthenticatedUser() throws Exception {
		restUserMockMvc.perform(get("/api/authenticate").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void testAuthenticatedUser() throws Exception {
		restUserMockMvc.perform(get("/api/authenticate").with(request -> {
			request.setRemoteUser("test");
			return request;
		}).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("test"));
	}

	@Test
	public void testGetExistingAccount() throws Exception {
		Set<Authority> authorities = new HashSet<>();
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.ADMIN);
		authorities.add(authority);

		Usuario user = new Usuario();
		user.setLogin("test");
		user.setNombre("john");
		user.setApellidos("doe");
		user.setEmail("john.doe@jhipster.com");
		user.seturlImagen("http://placehold.it/50x50");
		user.setIdioma("en");
		user.setAuthorities(authorities);
		when(mockUserService.getUsuarioWithAuthorities()).thenReturn(user);

		restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.login").value("test")).andExpect(jsonPath("$.nombre").value("john"))
				.andExpect(jsonPath("$.apellidos").value("doe"))
				.andExpect(jsonPath("$.email").value("john.doe@jhipster.com"))
				.andExpect(jsonPath("$.urlImagen").value("http://placehold.it/50x50"))
				.andExpect(jsonPath("$.idioma").value("en"))
				.andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
	}

	@Test
	public void testGetUnknownAccount() throws Exception {
		when(mockUserService.getUsuarioWithAuthorities()).thenReturn(null);

		restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	@Transactional
	@WithMockUser("save-account")
	public void testSaveAccount() throws Exception {
		Usuario user = new Usuario();
		user.setLogin("save-account");
		user.setEmail("save-account@example.com");
		user.setActivado(true);

		userRepository.saveAndFlush(user);
		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(null)
				.setLogin("not-used")
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("save-account@example.com")
				.setActivated(false)
				.setImageUrl("http://placehold.it/50x50")
				.setLangKey("en")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)))
				.build();
		//@formatter:on

		restMvc.perform(post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());

		Usuario updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
		assertThat(updatedUser.getNombre()).isEqualTo(userDTO.getNombre());
		assertThat(updatedUser.getApellidos()).isEqualTo(userDTO.getApellidos());
		assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
		assertThat(updatedUser.getIdioma()).isEqualTo(userDTO.getIdioma());
		assertThat(updatedUser.getUrlImagen()).isEqualTo(userDTO.getUrlImagen());
		assertThat(updatedUser.getActivado()).isEqualTo(true);
		assertThat(updatedUser.getAuthorities()).isEmpty();
	}

	@Test
	@Transactional
	@WithMockUser("save-invalid-email")
	public void testSaveInvalidEmail() throws Exception {
		Usuario user = new Usuario();
		user.setLogin("save-invalid-email");
		user.setEmail("save-invalid-email@example.com");
		user.setActivado(true);

		userRepository.saveAndFlush(user);

		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(null)
				.setLogin("not-used")
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("invalid email")
				.setActivated(false)
				.setImageUrl("http://placehold.it/50x50")
				.setLangKey("en")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)))
				.build();
		//@formatter:on

		restMvc.perform(post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());

		assertThat(userRepository.findOneByEmail("invalid email")).isNotPresent();
	}

	@Test
	@Transactional
	@WithMockUser("save-existing-email")
	public void testSaveExistingEmail() throws Exception {
		Usuario user = new Usuario();
		user.setLogin("save-existing-email");
		user.setEmail("save-existing-email@example.com");
		user.setActivado(true);

		userRepository.saveAndFlush(user);

		Usuario anotherUser = new Usuario();
		anotherUser.setLogin("save-existing-email2");
		anotherUser.setEmail("save-existing-email2@example.com");
		anotherUser.setActivado(true);

		userRepository.saveAndFlush(anotherUser);
		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(null)
				.setLogin("not-used")
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("save-existing-email2@example.com")
				.setActivated(false)
				.setImageUrl("http://placehold.it/50x50")
				.setLangKey("en")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)))
				.build();
		//@formatter:on

		restMvc.perform(post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());

		Usuario updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
		assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
	}

	@Test
	@Transactional
	@WithMockUser("save-existing-email-and-login")
	public void testSaveExistingEmailAndLogin() throws Exception {
		Usuario user = new Usuario();
		user.setLogin("save-existing-email-and-login");
		user.setEmail("save-existing-email-and-login@example.com");
		user.setActivado(true);

		userRepository.saveAndFlush(user);
		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(null)
				.setLogin("not-used")
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("save-existing-email-and-login@example.com")
				.setActivated(false)
				.setImageUrl("http://placehold.it/50x50")
				.setLangKey("en")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)))
				.build();
		//@formatter:on

		restMvc.perform(post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());

		Usuario updatedUser = userRepository.findOneByLogin("save-existing-email-and-login").orElse(null);
		assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email-and-login@example.com");
	}

}
