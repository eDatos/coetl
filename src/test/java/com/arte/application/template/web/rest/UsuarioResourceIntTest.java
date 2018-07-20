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
import com.arte.application.template.errors.ExceptionTranslator;
import com.arte.application.template.repository.UsuarioRepository;
import com.arte.application.template.service.LdapService;
import com.arte.application.template.service.MailService;
import com.arte.application.template.service.UsuarioService;
import com.arte.application.template.web.rest.dto.UsuarioDTO;
import com.arte.application.template.web.rest.mapper.UsuarioMapper;
import com.arte.application.template.web.rest.vm.ManagedUserVM;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
public class UsuarioResourceIntTest {

    private static final String ENDPOINT_URL = "/api/usuarios";

    private static final String DEFAULT_LOGIN_NEW_USER = "johndoe";
    private static final String DEFAULT_LOGIN_EXISTING_USER = "alice";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_NOMBRE = "john";
    private static final String DEFAULT_PRIMER_APELLIDO = "doe";

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

    private Usuario newUser;
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
    public static Usuario createEntity(String login) {
        Usuario user = new Usuario();
        user.setLogin(login);
        user.setEmail(DEFAULT_EMAIL);
        user.setNombre(DEFAULT_NOMBRE);
        user.setApellido1(DEFAULT_PRIMER_APELLIDO);
        return user;
    }

    @Before
    public void initTest() {
        newUser = createEntity(DEFAULT_LOGIN_NEW_USER);
        existingUser = createEntity(DEFAULT_LOGIN_EXISTING_USER);
        em.persist(existingUser);
    }

    @Test
    @Transactional
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();
        Mockito.when(ldapService.buscarUsuarioLdap(Mockito.anyString())).thenReturn(new UsuarioLdapEntry());

        Set<Rol> authorities = mockRolesDTO();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(newUser);
        source.setRoles(authorities);
        managedUserVM.updateFrom(source);

        restUserMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isCreated());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId())).collect(Collectors.toList());
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUser = userList.get(0);
        assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN_NEW_USER);
        assertThat(testUser.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUser.getApellido1()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createUserWithExistingId() throws Exception {
        userRepository.save(newUser);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Usuario userWithExistingId = userRepository.findOne(newUser.getId());
        userWithExistingId.setLogin("anotherlogin");
        userWithExistingId.setEmail("anothermail@localhost");

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(userWithExistingId);
        managedUserVM.updateFrom(source);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.save(newUser);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Usuario userWithExistingLogin = new Usuario();
        userWithExistingLogin.setId(null);
        userWithExistingLogin.setLogin(newUser.getLogin());
        userWithExistingLogin.setNombre("anothername");
        userWithExistingLogin.setApellido1("anotherelastname");
        userWithExistingLogin.setEmail("another@localhost");

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(userWithExistingLogin);
        managedUserVM.updateFrom(source);

        // Create the User
        restUserMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.save(newUser);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Usuario userWithExistingEmail = new Usuario();
        userWithExistingEmail.setId(null);
        userWithExistingEmail.setLogin("anotherlogin");
        userWithExistingEmail.setNombre("anothername");
        userWithExistingEmail.setApellido1("anotherelastname");
        userWithExistingEmail.setEmail(newUser.getEmail());

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(userWithExistingEmail);
        managedUserVM.updateFrom(source);

        // Create the User
        restUserMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());

        // Validate the User in the database
        List<Usuario> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.save(newUser);

        // Get all the users
        restUserMockMvc.perform(get("/api/usuarios?sort=id,desc").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN_NEW_USER))).andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
                .andExpect(jsonPath("$.[*].apellido1").value(hasItem(DEFAULT_PRIMER_APELLIDO))).andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void getUser() throws Exception {
        // Initialize the database
        userRepository.save(newUser);

        // Get the user
        restUserMockMvc.perform(get("/api/usuarios/{login}", newUser.getLogin())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.login").value(newUser.getLogin())).andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE)).andExpect(jsonPath("$.apellido1").value(DEFAULT_PRIMER_APELLIDO))
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

        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        Usuario updatedUser = userRepository.findOne(existingUser.getId());

        ManagedUserVM managedUserVM = new ManagedUserVM();
        UsuarioDTO source = usuarioMapper.userToUserDTO(updatedUser);
        source.setLogin("daniel");
        source.setNombre("Daniel");
        source.setApellido1("Smith");
        source.setApellido2("Down");
        source.setEmail("email@email.com");
        managedUserVM.updateFrom(source);

        restUserMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isOk());

        List<Usuario> userList = userRepository.findAll().stream().sorted((u1, u2) -> u2.getId().compareTo(u1.getId())).collect(Collectors.toList());
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUser = userList.get(0);
        assertThat(testUser.getLogin()).isEqualTo(source.getLogin());
        assertThat(testUser.getNombre()).isEqualTo(source.getNombre());
        assertThat(testUser.getApellido1()).isEqualTo(source.getApellido1());
        assertThat(testUser.getApellido2()).isEqualTo(source.getApellido2());
        assertThat(testUser.getEmail()).isEqualTo(source.getEmail());
    }

    @Test
    @Transactional
    public void updateUserExistingEmail() throws Exception {

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

        restUserMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateUserExistingLogin() throws Exception {

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
        restUserMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(managedUserVM))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception {
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
    public void testUserFromId() {
        assertThat(userMapper.userFromId(existingUser.getId()).getId()).isEqualTo(existingUser.getId());
        assertThat(userMapper.userFromId(null)).isNull();
    }

    @Test
    @Transactional
    public void testUserDTOtoUser() {
        //@formatter:off
		ManagedUserVM managedUserVM = new ManagedUserVM();
		UsuarioDTO source = UsuarioDTO.builder()
				.setId(1l)
				.setLogin(DEFAULT_LOGIN_NEW_USER)
				.setFirstName(DEFAULT_NOMBRE)
				.setLastName(DEFAULT_PRIMER_APELLIDO)
				.setEmail(DEFAULT_EMAIL)
				.setCreatedBy(DEFAULT_LOGIN_NEW_USER)
				.setCreatedDate(null)
				.setLastModifiedBy(DEFAULT_LOGIN_NEW_USER)
				.setLastModifiedDate(null)
				.setAuthorities(mockRolesDTO())
				.build();
		managedUserVM.updateFrom(source);
		//@formatter:on
        Usuario user = userMapper.userDTOToUser(source);
        assertThat(user.getId()).isEqualTo(source.getId());
        assertThat(user.getLogin()).isEqualTo(source.getLogin());
        assertThat(user.getNombre()).isEqualTo(source.getNombre());
        assertThat(user.getApellido1()).isEqualTo(source.getApellido1());
        assertThat(user.getEmail()).isEqualTo(source.getEmail());
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getRoles()).containsExactly(Rol.ADMIN);
    }

    @Test
    @Transactional
    public void testUserToUserDTO() {
        UsuarioDTO userDTO = userMapper.userToUserDTO(newUser);
        assertThat(userDTO.getId()).isEqualTo(newUser.getId());
        assertThat(userDTO.getLogin()).isEqualTo(newUser.getLogin());
        assertThat(userDTO.getNombre()).isEqualTo(newUser.getNombre());
        assertThat(userDTO.getApellido1()).isEqualTo(newUser.getApellido1());
        assertThat(userDTO.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(userDTO.getCreatedBy()).isEqualTo(newUser.getCreatedBy());
        assertThat(userDTO.getCreatedDate()).isEqualTo(newUser.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(newUser.getLastModifiedBy());
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(newUser.getLastModifiedDate());
        assertEquals(userDTO.getRoles(), newUser.getRoles());
        assertThat(userDTO.toString()).isNotNull();
    }
}
