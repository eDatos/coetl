package es.tenerife.secretaria.libro.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.SecretariaLibroApp;
import es.tenerife.secretaria.libro.config.audit.AuditEventPublisher;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.Usuario;
import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.security.AuthoritiesConstants;
import es.tenerife.secretaria.libro.service.LdapService;
import es.tenerife.secretaria.libro.service.MailService;
import es.tenerife.secretaria.libro.service.UsuarioService;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;
import es.tenerife.secretaria.libro.web.rest.dto.UsuarioDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.RolMapper;
import es.tenerife.secretaria.libro.web.rest.mapper.UsuarioMapper;

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
	private RolRepository rolRepository;

	@Autowired
	private RolMapper rolMapper;

	@Autowired
	private UsuarioService userService;

	@Autowired
	private UsuarioMapper usuarioMapper;

	@SuppressWarnings("rawtypes")
	@Autowired
	private HttpMessageConverter[] httpMessageConverters;

	@Mock
	private UsuarioService mockUserService;

	@Mock
	private MailService mockMailService;

	@MockBean
	private LdapService ldapService;

	@Autowired
	private AuditEventPublisher auditPublisher;

	private MockMvc restUserMockMvc;

	private MockMvc restMvc;

	private RolDTO mockRolDTO() {
		RolDTO rolDTO = new RolDTO();
		rolDTO.setCodigo(AuthoritiesConstants.ADMIN);
		rolDTO.setNombre(AuthoritiesConstants.ADMIN);
		return rolDTO;
	}

	private HashSet<RolDTO> mockRolSet() {
		return new HashSet<>(Collections.singletonList(mockRolDTO()));
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		UsuarioResource accountResource = new UsuarioResource(userRepository, null, userService, usuarioMapper, null,
				auditPublisher);

		UsuarioResource accountUserMockResource = new UsuarioResource(userRepository, null, mockUserService,
				usuarioMapper, null, auditPublisher);

		this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).setMessageConverters(httpMessageConverters)
				.build();
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
	}

	@Test
	public void testNonautenticardUser() throws Exception {
		restUserMockMvc.perform(get("/api/autenticar").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void testautenticardUser() throws Exception {
		restUserMockMvc.perform(get("/api/autenticar").with(request -> {
			request.setRemoteUser("test");
			return request;
		}).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("test"));
	}

	@Test
	@Transactional
	public void testGetExistingAccount() throws Exception {
		Set<Rol> authorities = new HashSet<>();
		Rol authority = rolRepository.findOneByCodigo(AuthoritiesConstants.ADMIN);
		authorities.add(authority);

		Usuario user = new Usuario();
		user.setLogin("test");
		user.setNombre("john");
		user.setApellido1("doe");
		user.setEmail("john.doe@jhipster.com");
		user.setRoles(authorities);
		when(mockUserService.getUsuarioWithAuthorities()).thenReturn(user);

		restUserMockMvc.perform(get("/api/usuario").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.login").value("test")).andExpect(jsonPath("$.nombre").value("john"))
				.andExpect(jsonPath("$.apellido1").value("doe"))
				.andExpect(jsonPath("$.email").value("john.doe@jhipster.com"))
				.andExpect(jsonPath("$.roles[*].codigo").value(AuthoritiesConstants.ADMIN));
	}

	@Test
	public void testGetUnknownAccount() throws Exception {
		when(mockUserService.getUsuarioWithAuthorities()).thenReturn(null);

		restUserMockMvc.perform(get("/api/usuario").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	@Transactional
	@WithMockUser("save-account")
	public void testSaveAccount() throws Exception {
		Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

		Usuario user = new Usuario();
		user.setLogin("save-account");
		user.setEmail("save-account@example.com");

		userRepository.saveAndFlush(user);
		HashSet<RolDTO> mockRolSet = mockRolSet();
		rolRepository.save(mockRolSet.stream().findFirst().map(rolMapper::toEntity).get());
		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(user.getId())
				.setOptLock(user.getOptLock())
				.setLogin(user.getLogin())
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("save-account@example.com")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolSet)
				.build();
		//@formatter:on

		restMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());

		Usuario updatedUser = userRepository.findOneByLogin(user.getLogin()).orElse(null);
		assertThat(updatedUser.getNombre()).isEqualTo(userDTO.getNombre());
		assertThat(updatedUser.getApellido1()).isEqualTo(userDTO.getApellido1());
		assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
		assertThat(updatedUser.getRoles()).size().isEqualTo(1);
		assertThat(((Rol) updatedUser.getRoles().toArray()[0]).getCodigo())
				.isEqualTo(((RolDTO) mockRolSet.toArray()[0]).getCodigo());

	}

	@Test
	@Transactional
	@WithMockUser("save-invalid-email")
	public void testSaveInvalidEmail() throws Exception {
		Usuario user = new Usuario();
		user.setLogin("save-invalid-email");
		user.setEmail("save-invalid-email@example.com");

		userRepository.saveAndFlush(user);

		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(user.getId())
				.setLogin(user.getLogin())
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("invalid email")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolSet())
				.build();
		//@formatter:on

		restMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
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

		userRepository.saveAndFlush(user);

		Usuario anotherUser = new Usuario();
		anotherUser.setLogin("save-existing-email2");
		anotherUser.setEmail("save-existing-email2@example.com");

		userRepository.saveAndFlush(anotherUser);
		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(user.getId())
				.setLogin(user.getLogin())
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("save-existing-email2@example.com")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolSet())
				.build();
		//@formatter:on

		restMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isBadRequest());

		Usuario updatedUser = userRepository.findOneByLogin("save-existing-email").orElse(null);
		assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
	}

	@Test
	@Transactional
	@WithMockUser("save-existing-email-and-login")
	public void testSaveExistingEmailAndLogin() throws Exception {
		Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

		Usuario user = new Usuario();
		user.setLogin("save-existing-email-and-login");
		user.setEmail("save-existing-email-and-login@example.com");

		userRepository.saveAndFlush(user);
		//@formatter:off
		UsuarioDTO userDTO = UsuarioDTO.builder()
				.setId(user.getId())
				.setOptLock(user.getOptLock())
				.setLogin(user.getLogin())
				.setFirstName("firstname")
				.setLastName("lastname")
				.setEmail("save-existing-email-and-login@example.com")
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(null)
				.build();
		//@formatter:on

		restMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(status().isOk());

		Usuario updatedUser = userRepository.findOneByLogin("save-existing-email-and-login").orElse(null);
		assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email-and-login@example.com");
	}

}
