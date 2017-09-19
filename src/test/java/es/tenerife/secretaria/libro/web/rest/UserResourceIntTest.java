package es.tenerife.secretaria.libro.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import es.tenerife.secretaria.libro.web.rest.errors.ExceptionTranslator;
import es.tenerife.secretaria.libro.web.rest.mapper.RolMapper;
import es.tenerife.secretaria.libro.web.rest.mapper.UsuarioMapper;
import es.tenerife.secretaria.libro.web.rest.vm.ManagedUserVM;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class UserResourceIntTest {

	private static final Long DEFAULT_ID = 1L;

	private static final String DEFAULT_LOGIN = "johndoe";
	private static final String UPDATED_LOGIN = "jhipster";

	private static final String DEFAULT_EMAIL = "johndoe@localhost";
	private static final String UPDATED_EMAIL = "jhipster@localhost";

	private static final String DEFAULT_FIRSTNAME = "john";
	private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

	private static final String DEFAULT_LASTNAME = "doe";
	private static final String UPDATED_LASTNAME = "jhipsterLastName";

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private RolRepository rolRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private UsuarioService userService;

	@Autowired
	private UsuarioMapper userMapper;

	@Autowired
	private RolMapper rolMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	@MockBean
	private LdapService ldapService;

	private MockMvc restUserMockMvc;

	private Usuario user;

	@Autowired
	private AuditEventPublisher auditPublisher;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		UsuarioResource userResource = new UsuarioResource(userRepository, mailService, userService, userMapper,
				ldapService, auditPublisher);
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	private Set<RolDTO> mockRolesDTO() {
		return mockRolesDTO(false);
	}

	private Set<RolDTO> mockRolesDTO(boolean save) {
		// Create the User
		Set<RolDTO> authorities = new HashSet<>();
		RolDTO rolDTO = new RolDTO();
		rolDTO.setCodigo(AuthoritiesConstants.ADMIN);
		rolDTO.setNombre(AuthoritiesConstants.ADMIN);
		if (save) {
			em.persist(rolMapper.toEntity(rolDTO));
		}
		authorities.add(rolDTO);
		return authorities;
	}

	/**
	 * Create a User.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which has a required relationship to the User entity.
	 */
	public static Usuario createEntity(EntityManager em) {
		Usuario user = new Usuario();
		user.setLogin(DEFAULT_LOGIN);
		user.setActivado(true);
		user.setEmail(DEFAULT_EMAIL);
		user.setNombre(DEFAULT_FIRSTNAME);
		user.setApellido1(DEFAULT_LASTNAME);
		return user;
	}

	@Before
	public void initTest() {
		user = createEntity(em);
	}

	@Test
	@Transactional
	public void createUser() throws Exception {
		int databaseSizeBeforeCreate = userRepository.findAll().size();
		Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

		Set<RolDTO> authorities = mockRolesDTO(true);
		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(null)
				.setLogin(DEFAULT_LOGIN)
				.setFirstName(DEFAULT_FIRSTNAME)
				.setLastName(DEFAULT_LASTNAME)
				.setEmail(DEFAULT_EMAIL)
				.setActivated(true)
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(authorities)
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on

		restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isCreated());

		// Validate the User in the database
		List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId()))
				.collect(Collectors.toList());
		assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
		Usuario testUser = userList.get(0);
		assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
		assertThat(testUser.getNombre()).isEqualTo(DEFAULT_FIRSTNAME);
		assertThat(testUser.getApellido1()).isEqualTo(DEFAULT_LASTNAME);
		assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
	}

	@Test
	@Transactional
	public void createUserWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = userRepository.findAll().size();

		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(1L)
				.setLogin(DEFAULT_LOGIN)
				.setFirstName(DEFAULT_FIRSTNAME)
				.setLastName(DEFAULT_LASTNAME)
				.setEmail(DEFAULT_EMAIL)
				.setActivated(true)
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolesDTO())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on

		// An entity with an existing ID cannot be created, so this API call must fail
		restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

		// Validate the User in the database
		List<Usuario> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createUserWithExistingLogin() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);
		int databaseSizeBeforeCreate = userRepository.findAll().size();

		Set<Long> authorities = new HashSet<>();
		authorities.add(1L);
		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(null)
				.setLogin(DEFAULT_LOGIN)
				.setFirstName(DEFAULT_FIRSTNAME)
				.setLastName(DEFAULT_LASTNAME)
				.setEmail("anothermail@localhost")
				.setActivated(true)
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolesDTO())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on

		// Create the User
		restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

		// Validate the User in the database
		List<Usuario> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createUserWithExistingEmail() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);
		int databaseSizeBeforeCreate = userRepository.findAll().size();

		Set<Long> authorities = new HashSet<>();
		authorities.add(1L);

		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(null)
				.setLogin("anotherlogin")
				.setFirstName(DEFAULT_FIRSTNAME)
				.setLastName(DEFAULT_LASTNAME)
				.setEmail(DEFAULT_EMAIL)
				.setActivated(true)
				.setCreatedBy(null)
				.setCreatedDate(null)
				.setLastModifiedBy(null)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolesDTO())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on

		// Create the User
		restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

		// Validate the User in the database
		List<Usuario> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllUsers() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		// Get all the users
		restUserMockMvc.perform(get("/api/usuarios?sort=id,desc").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
				.andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_FIRSTNAME)))
				.andExpect(jsonPath("$.[*].apellido1").value(hasItem(DEFAULT_LASTNAME)))
				.andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
	}

	@Test
	@Transactional
	public void getUser() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		// Get the user
		restUserMockMvc.perform(get("/api/usuarios/{login}", user.getLogin())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.login").value(user.getLogin()))
				.andExpect(jsonPath("$.nombre").value(DEFAULT_FIRSTNAME))
				.andExpect(jsonPath("$.apellido1").value(DEFAULT_LASTNAME))
				.andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
	}

	@Test
	@Transactional
	public void getNonExistingUser() throws Exception {
		restUserMockMvc.perform(get("/api/usuarios/unknown")).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateUser() throws Exception {
		Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

		// Initialize the database
		userRepository.saveAndFlush(user);
		int databaseSizeBeforeUpdate = userRepository.findAll().size();

		// Update the user
		Usuario updatedUser = userRepository.findOne(user.getId());

		Set<Long> authorities = new HashSet<>();
		authorities.add(1L);
		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(updatedUser.getId())
				.setLogin(updatedUser.getLogin())
				.setFirstName(UPDATED_FIRSTNAME)
				.setLastName(UPDATED_LASTNAME)
				.setEmail(UPDATED_EMAIL)
				.setActivated(updatedUser.getActivado())
				.setCreatedBy(updatedUser.getCreatedBy())
				.setCreatedDate(updatedUser.getCreatedDate())
				.setLastModifiedBy(updatedUser.getLastModifiedBy())
				.setLastModifiedDate(updatedUser.getLastModifiedDate())
				.setAuthorities(mockRolesDTO())
				.setOptLock(updatedUser.getOptLock())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on

		restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isOk());

		// Validate the User in the database
		List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId()))
				.collect(Collectors.toList());
		assertThat(userList).hasSize(databaseSizeBeforeUpdate);
		Usuario testUser = userList.get(0);
		assertThat(testUser.getNombre()).isEqualTo(UPDATED_FIRSTNAME);
		assertThat(testUser.getApellido1()).isEqualTo(UPDATED_LASTNAME);
		assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
	}

	@Test
	@Transactional
	public void updateUserLogin() throws Exception {
		Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

		// Initialize the database
		userRepository.saveAndFlush(user);
		int databaseSizeBeforeUpdate = userRepository.findAll().size();

		// Update the user
		Usuario updatedUser = userRepository.findOne(user.getId());

		Set<Long> authorities = new HashSet<>();
		authorities.add(1L);
		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(updatedUser.getId())
				.setLogin(UPDATED_LOGIN)
				.setFirstName(UPDATED_FIRSTNAME)
				.setLastName(UPDATED_LASTNAME)
				.setEmail(UPDATED_EMAIL)
				.setActivated(updatedUser.getActivado())
				.setCreatedBy(updatedUser.getCreatedBy())
				.setCreatedDate(updatedUser.getCreatedDate())
				.setLastModifiedBy(updatedUser.getLastModifiedBy())
				.setLastModifiedDate(updatedUser.getLastModifiedDate())
				.setAuthorities(mockRolesDTO())
				.setOptLock(updatedUser.getOptLock())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on

		restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isOk());

		// Validate the User in the database
		List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId()))
				.collect(Collectors.toList());
		assertThat(userList).hasSize(databaseSizeBeforeUpdate);
		Usuario testUser = userList.get(0);
		assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
		assertThat(testUser.getNombre()).isEqualTo(UPDATED_FIRSTNAME);
		assertThat(testUser.getApellido1()).isEqualTo(UPDATED_LASTNAME);
		assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
	}

	@Test
	@Transactional
	public void updateUserExistingEmail() throws Exception {
		// Initialize the database with 2 users
		userRepository.saveAndFlush(user);

		Usuario anotherUser = new Usuario();
		anotherUser.setLogin("jhipster");
		anotherUser.setActivado(true);
		anotherUser.setEmail("jhipster@localhost");
		anotherUser.setNombre("java");
		anotherUser.setApellido1("hipster");
		userRepository.saveAndFlush(anotherUser);

		// Update the user
		Usuario updatedUser = userRepository.findOne(user.getId());

		Set<Long> authorities = new HashSet<>();
		authorities.add(1L);
		//@formatter:off
				ManagedUserVM managedUserVM = new ManagedUserVM();
				UsuarioDTO source = UsuarioDTO.builder()
						.setId(updatedUser.getId())
						.setLogin( updatedUser.getLogin())
						.setFirstName(updatedUser.getNombre())
						.setLastName(updatedUser.getApellido1())
						.setEmail("jhipster@localhost")
						.setActivated(updatedUser.getActivado())
						.setCreatedBy(updatedUser.getCreatedBy())
						.setCreatedDate(updatedUser.getCreatedDate())
						.setLastModifiedBy(updatedUser.getLastModifiedBy())
						.setLastModifiedDate(updatedUser.getLastModifiedDate())
						.setAuthorities(mockRolesDTO())
						.build();
				managedUserVM.updateFrom(source);
				//@formatter:on

		restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void updateUserExistingLogin() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		Usuario anotherUser = new Usuario();
		anotherUser.setLogin("jhipster");
		anotherUser.setActivado(true);
		anotherUser.setEmail("jhipster@localhost");
		anotherUser.setNombre("java");
		anotherUser.setApellido1("hipster");
		userRepository.saveAndFlush(anotherUser);

		// Update the user
		Usuario updatedUser = userRepository.findOne(user.getId());

		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(updatedUser.getId())
				.setLogin("")
				.setFirstName(updatedUser.getNombre())
				.setLastName(updatedUser.getApellido1())
				.setEmail("jhipster@localhost")
				.setActivated(updatedUser.getActivado())
				.setCreatedBy(updatedUser.getCreatedBy())
				.setCreatedDate(updatedUser.getCreatedDate())
				.setLastModifiedBy(updatedUser.getLastModifiedBy())
				.setLastModifiedDate(updatedUser.getLastModifiedDate())
				.setAuthorities(mockRolesDTO())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on
		restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void deleteUser() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);
		int databaseSizeBeforeDelete = userRepository.findAll().size();

		// Delete the user
		restUserMockMvc.perform(delete("/api/usuarios/{login}", user.getLogin()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Usuario> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeDelete);
		Usuario deleted = userRepository.findOne(user.getId());
		assertThat(deleted.getDeletionDate()).isNotNull();
	}

	@Test
	@Transactional
	public void testUserEquals() throws Exception {
		TestUtil.equalsVerifier(Usuario.class);
		Usuario user1 = new Usuario();
		user1.setId(1L);
		Usuario user2 = new Usuario();
		user2.setId(user1.getId());
		assertThat(user1).isEqualTo(user2);
		user2.setId(2L);
		assertThat(user1).isNotEqualTo(user2);
		user1.setId(null);
		assertThat(user1).isNotEqualTo(user2);
	}

	@Test
	public void testUserFromId() {
		assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
		assertThat(userMapper.userFromId(null)).isNull();
	}

	@Test
	@Transactional
	public void testUserDTOtoUser() {

		//@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(DEFAULT_ID)
				.setLogin(DEFAULT_LOGIN)
				.setFirstName(DEFAULT_FIRSTNAME)
				.setLastName(DEFAULT_LASTNAME)
				.setEmail(DEFAULT_EMAIL)
				.setActivated(true)
				.setCreatedBy(DEFAULT_LOGIN)
				.setCreatedDate(null)
				.setLastModifiedBy(DEFAULT_LOGIN)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolesDTO())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on
		Usuario user = userMapper.userDTOToUser(source);
		assertThat(user.getId()).isEqualTo(DEFAULT_ID);
		assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
		assertThat(user.getNombre()).isEqualTo(DEFAULT_FIRSTNAME);
		assertThat(user.getApellido1()).isEqualTo(DEFAULT_LASTNAME);
		assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(user.getActivado()).isEqualTo(true);
		assertThat(user.getCreatedBy()).isNull();
		assertThat(user.getCreatedDate()).isNotNull();
		assertThat(user.getLastModifiedBy()).isNull();
		assertThat(user.getLastModifiedDate()).isNotNull();
		assertThat(user.getRoles()).extracting("codigo").containsExactly(AuthoritiesConstants.ADMIN);
	}

	@Test
	@Transactional
	public void testUserToUserDTO() {
		user.setId(DEFAULT_ID);
		user.setCreatedBy(DEFAULT_LOGIN);
		user.setCreatedDate(Instant.now());
		user.setLastModifiedBy(DEFAULT_LOGIN);
		user.setLastModifiedDate(Instant.now());

		Set<Rol> authorities = new HashSet<>();
		Rol authority = rolRepository.findOneByCodigo(AuthoritiesConstants.USER);

		authorities.add(authority);
		user.setRoles(authorities);

		UsuarioDTO userDTO = userMapper.userToUserDTO(user);

		assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
		assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
		assertThat(userDTO.getNombre()).isEqualTo(DEFAULT_FIRSTNAME);
		assertThat(userDTO.getApellido1()).isEqualTo(DEFAULT_LASTNAME);
		assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(userDTO.isActivado()).isEqualTo(true);
		assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
		assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
		assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
		assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
		userDTO.getRoles().forEach((rol) -> assertThat(rol.getCodigo()).isEqualTo(authority.getCodigo()));
		assertThat(userDTO.toString()).isNotNull();
	}

	@Test
	public void testAuthorityEquals() throws Exception {
		Rol authorityA = new Rol();
		assertThat(authorityA).isEqualTo(authorityA);
		assertThat(authorityA).isNotEqualTo(null);
		assertThat(authorityA).isNotEqualTo(new Object());
		assertThat(authorityA.hashCode()).isEqualTo(0);
		assertThat(authorityA.toString()).isNotNull();

		Rol authorityB = new Rol();
		assertThat(authorityA).isEqualTo(authorityB);

		authorityB.setNombre(AuthoritiesConstants.ADMIN);
		assertThat(authorityA).isNotEqualTo(authorityB);

		authorityA.setNombre(AuthoritiesConstants.USER);
		assertThat(authorityA).isNotEqualTo(authorityB);

		authorityB.setNombre(AuthoritiesConstants.USER);
		assertThat(authorityA).isEqualTo(authorityB);
		assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
	}

}
