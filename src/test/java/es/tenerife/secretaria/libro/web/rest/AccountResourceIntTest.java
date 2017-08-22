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
import es.tenerife.secretaria.libro.domain.User;
import es.tenerife.secretaria.libro.repository.UserRepository;
import es.tenerife.secretaria.libro.security.AuthoritiesConstants;
import es.tenerife.secretaria.libro.service.MailService;
import es.tenerife.secretaria.libro.service.UserService;
import es.tenerife.secretaria.libro.web.rest.dto.UserDTO;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class AccountResourceIntTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@SuppressWarnings("rawtypes")
	@Autowired
	private HttpMessageConverter[] httpMessageConverters;

	@Mock
	private UserService mockUserService;

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

		User user = new User();
		user.setLogin("test");
		user.setFirstName("john");
		user.setLastName("doe");
		user.setEmail("john.doe@jhipster.com");
		user.setImageUrl("http://placehold.it/50x50");
		user.setLangKey("en");
		user.setAuthorities(authorities);
		when(mockUserService.getUserWithAuthorities()).thenReturn(user);

		restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.login").value("test")).andExpect(jsonPath("$.firstName").value("john"))
				.andExpect(jsonPath("$.lastName").value("doe"))
				.andExpect(jsonPath("$.email").value("john.doe@jhipster.com"))
				.andExpect(jsonPath("$.imageUrl").value("http://placehold.it/50x50"))
				.andExpect(jsonPath("$.langKey").value("en"))
				.andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
	}

	@Test
	public void testGetUnknownAccount() throws Exception {
		when(mockUserService.getUserWithAuthorities()).thenReturn(null);

		restUserMockMvc.perform(get("/api/account").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	@Transactional
	@WithMockUser("save-account")
	public void testSaveAccount() throws Exception {
		User user = new User();
		user.setLogin("save-account");
		user.setEmail("save-account@example.com");
		user.setActivated(true);

		userRepository.saveAndFlush(user);
		//@formatter:off
		UserDTO userDTO = UserDTO.builder()
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

		User updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
		assertThat(updatedUser.getFirstName()).isEqualTo(userDTO.getFirstName());
		assertThat(updatedUser.getLastName()).isEqualTo(userDTO.getLastName());
		assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
		assertThat(updatedUser.getLangKey()).isEqualTo(userDTO.getLangKey());
		assertThat(updatedUser.getImageUrl()).isEqualTo(userDTO.getImageUrl());
		assertThat(updatedUser.getActivated()).isEqualTo(true);
		assertThat(updatedUser.getAuthorities()).isEmpty();
	}

	@Test
	@Transactional
	@WithMockUser("save-invalid-email")
	public void testSaveInvalidEmail() throws Exception {
		User user = new User();
		user.setLogin("save-invalid-email");
		user.setEmail("save-invalid-email@example.com");
		user.setActivated(true);

		userRepository.saveAndFlush(user);

		//@formatter:off
		UserDTO userDTO = UserDTO.builder()
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
		User user = new User();
		user.setLogin("save-existing-email");
		user.setEmail("save-existing-email@example.com");
		user.setActivated(true);

		userRepository.saveAndFlush(user);

		User anotherUser = new User();
		anotherUser.setLogin("save-existing-email2");
		anotherUser.setEmail("save-existing-email2@example.com");
		anotherUser.setActivated(true);

		userRepository.saveAndFlush(anotherUser);
		//@formatter:off
		UserDTO userDTO = UserDTO.builder()
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

		User updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
		assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
	}

	@Test
	@Transactional
	@WithMockUser("save-existing-email-and-login")
	public void testSaveExistingEmailAndLogin() throws Exception {
		User user = new User();
		user.setLogin("save-existing-email-and-login");
		user.setEmail("save-existing-email-and-login@example.com");
		user.setActivated(true);

		userRepository.saveAndFlush(user);
		//@formatter:off
		UserDTO userDTO = UserDTO.builder()
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

		User updatedUser = userRepository.findOneByLogin("save-existing-email-and-login").orElse(null);
		assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email-and-login@example.com");
	}

}
