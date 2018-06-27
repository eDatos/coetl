package com.arte.application.template.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.arte.application.template.ArteApplicationTemplateApp;
import com.arte.application.template.config.audit.AuditEventPublisher;
import com.arte.application.template.domain.Usuario;
import com.arte.application.template.domain.enumeration.Rol;
import com.arte.application.template.entry.UsuarioLdapEntry;
import com.arte.application.template.repository.UsuarioRepository;
import com.arte.application.template.service.LdapService;
import com.arte.application.template.service.MailService;
import com.arte.application.template.service.UsuarioService;
import com.arte.application.template.web.rest.dto.UsuarioDTO;
import com.arte.application.template.web.rest.errors.ExceptionTranslator;
import com.arte.application.template.web.rest.mapper.UsuarioMapper;
import com.arte.application.template.web.rest.vm.ManagedUserVM;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
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

    private static final String ROL_ADMIN = "ADMIN";

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    UsuarioMapper usuarioMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private UsuarioService userService;

    @Autowired
    private UsuarioMapper userMapper;

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

    private Usuario existingUser;

    @Autowired
    private AuditEventPublisher auditPublisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsuarioResource userResource = new UsuarioResource(userRepository, mailService, userService, userMapper, ldapService, auditPublisher);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    private Set<Rol> mockRolesDTO() {
        return Stream.of(Rol.ADMIN).collect(Collectors.toSet());
    }

    /**
     * Create a User.
     * This is a static method, as tests for other entities might also need it, if
     * they test an entity which has a required relationship to the User entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario user = new Usuario();
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        user.setNombre(DEFAULT_FIRSTNAME);
        user.setApellido1(DEFAULT_LASTNAME);
        return user;
    }

    @Before
    public void initTest() {
        existingUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();
        Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

        Set<Rol> authorities = mockRolesDTO();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(existingUser);
        source.setRoles(authorities);
        managedUserVM.updateFrom(source);

        restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isCreated());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId())).collect(Collectors.toList());
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
        userRepository.save(existingUser);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Usuario userWithExistingId = userRepository.findOne(existingUser.getId());
        userWithExistingId.setLogin("anotherlogin");
        userWithExistingId.setEmail("anothermail@localhost");

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(userWithExistingId);
        managedUserVM.updateFrom(source);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.save(existingUser);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Usuario userWithExistingLogin = new Usuario();
        userWithExistingLogin.setId(null);
        userWithExistingLogin.setLogin(existingUser.getLogin());
        userWithExistingLogin.setNombre("anothername");
        userWithExistingLogin.setApellido1("anotherelastname");
        userWithExistingLogin.setEmail("another@localhost");

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(userWithExistingLogin);
        managedUserVM.updateFrom(source);

        // Create the User
        restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.save(existingUser);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Usuario userWithExistingEmail = new Usuario();
        userWithExistingEmail.setId(null);
        userWithExistingEmail.setLogin("anotherlogin");
        userWithExistingEmail.setNombre("anothername");
        userWithExistingEmail.setApellido1("anotherelastname");
        userWithExistingEmail.setEmail(existingUser.getEmail());

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(userWithExistingEmail);
        managedUserVM.updateFrom(source);

        // Create the User
        restUserMockMvc.perform(post("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.save(existingUser);

        // Get all the users
        restUserMockMvc.perform(get("/api/usuarios?sort=id,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN))).andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_FIRSTNAME)))
                .andExpect(jsonPath("$.[*].apellido1").value(hasItem(DEFAULT_LASTNAME))).andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void getUser() throws Exception {
        // Initialize the database
        userRepository.save(existingUser);

        // Get the user
        restUserMockMvc.perform(get("/api/usuarios/{login}", existingUser.getLogin())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.login").value(existingUser.getLogin())).andExpect(jsonPath("$.nombre").value(DEFAULT_FIRSTNAME)).andExpect(jsonPath("$.apellido1").value(DEFAULT_LASTNAME))
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
        userRepository.save(existingUser);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        Usuario updatedUser = userRepository.findOne(existingUser.getId());

        //@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = usuarioMapper.userToUserDTO(updatedUser);
		source.setNombre(UPDATED_FIRSTNAME);
		source.setApellido1(UPDATED_LASTNAME);
		source.setEmail(UPDATED_EMAIL);
		managedUserVM.updateFrom(source);
		//@formatter:on

        restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isOk());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId())).collect(Collectors.toList());
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
        userRepository.save(existingUser);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        Usuario updatedUser = userRepository.findOne(existingUser.getId());

        //@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = usuarioMapper.userToUserDTO(updatedUser);
		source.setLogin(UPDATED_LOGIN);
        source.setNombre(UPDATED_FIRSTNAME);
        source.setApellido1(UPDATED_LASTNAME);
        source.setEmail(UPDATED_EMAIL);
		managedUserVM.updateFrom(source);
		//@formatter:on

        restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isOk());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId())).collect(Collectors.toList());
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
        userRepository.save(existingUser);

        Usuario anotherUser = new Usuario();
        anotherUser.setLogin("jhipster");
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setNombre("java");
        anotherUser.setApellido1("hipster");
        userRepository.save(anotherUser);

        // Update the user
        Usuario updatedUser = userRepository.findOne(existingUser.getId());

        //@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = usuarioMapper.userToUserDTO(updatedUser);
        source.setEmail(anotherUser.getEmail());
		managedUserVM.updateFrom(source);
		//@formatter:on

        restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateUserExistingLogin() throws Exception {
        // Initialize the database
        userRepository.save(existingUser);

        Usuario anotherUser = new Usuario();
        anotherUser.setLogin("jhipster");
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setNombre("java");
        anotherUser.setApellido1("hipster");
        userRepository.save(anotherUser);

        // Update the user
        Usuario updatedUser = userRepository.findOne(existingUser.getId());

        //@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = usuarioMapper.userToUserDTO(updatedUser);
        source.setLogin(anotherUser.getLogin());
		managedUserVM.updateFrom(source);
		//@formatter:on
        restUserMockMvc.perform(put("/api/usuarios").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception {
        // Initialize the database
        userRepository.save(existingUser);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc.perform(delete("/api/usuarios/{login}", existingUser.getLogin()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeDelete);
        Usuario deleted = userRepository.findOne(existingUser.getId());
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
    @Transactional
    public void testUserFromId() {
        userRepository.save(existingUser);
        assertThat(userMapper.userFromId(existingUser.getId()).getId()).isEqualTo(existingUser.getId());
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
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getRoles()).containsExactly(Rol.ADMIN);
    }

    @Test
    @Transactional
    public void testUserToUserDTO() {
        UsuarioDTO userDTO = userMapper.userToUserDTO(existingUser);

        assertThat(userDTO.getId()).isEqualTo(existingUser.getId());
        assertThat(userDTO.getLogin()).isEqualTo(existingUser.getLogin());
        assertThat(userDTO.getNombre()).isEqualTo(existingUser.getNombre());
        assertThat(userDTO.getApellido1()).isEqualTo(existingUser.getApellido1());
        assertThat(userDTO.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(userDTO.getCreatedBy()).isEqualTo(existingUser.getCreatedBy());
        assertThat(userDTO.getCreatedDate()).isEqualTo(existingUser.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(existingUser.getLastModifiedBy());
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(existingUser.getLastModifiedDate());
        assertEquals(userDTO.getRoles(), existingUser.getRoles());
        assertThat(userDTO.toString()).isNotNull();
    }
}
