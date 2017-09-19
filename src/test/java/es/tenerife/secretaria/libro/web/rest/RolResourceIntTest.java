package es.tenerife.secretaria.libro.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import es.tenerife.secretaria.libro.SecretariaLibroApp;
import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.enums.TipoAccionOperacion;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.service.RolService;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;
import es.tenerife.secretaria.libro.web.rest.mapper.RolMapper;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class RolResourceIntTest {

	private static final String SUJETO_ROL = "ROL";

	private static final String ADMIN_LOGIN = "ADMIN";

	private static final String TEST_ROL = "TEST_ROL";

	private static final String ROLE_ADMIN = "ADMIN";

	private static final String DEFAULT_LOGIN = "testLoginName";

	@Autowired
	private EntityManager em;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private RolMapper rolMapper;

	@SpyBean
	private RolService rolService;

	@MockBean
	private OperacionService operacionService;

	@Autowired
	private RolRepository rolRepository;

	private MockMvc restUserMockMvc;

	private Rol rol;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		rol = createEntity(em);
		this.restUserMockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

	}

	private Operacion mockOperacion() {
		Operacion operacion = new Operacion();
		Set<Rol> roles = new HashSet<>();
		roles.add(rol);
		operacion.setRoles(roles);
		return operacion;
	}

	public static Rol createEntity(EntityManager em) {
		Rol rol = new Rol();
		rol.setCodigo(ROLE_ADMIN);
		return rol;

	}

	private RolDTO createRolDTO() {
		RolDTO rolDTO = new RolDTO();
		rolDTO.setNombre(TEST_ROL);
		rolDTO.setCodigo(TEST_ROL);
		return rolDTO;
	}

	private Rol mockRol(String rolCode) {
		Rol rol = new Rol();
		rol.setCodigo(rolCode);
		rol.setNombre("ROL NAME " + rolCode);
		return rol;
	}

	private Set<Rol> mockRoles(Rol... roles) {
		Set<Rol> set = new HashSet<>();
		for (Rol rol : roles) {
			set.add(rol);
		}
		return set;
	}

	@Test
	@WithMockUser(roles = ROLE_ADMIN, username = ADMIN_LOGIN)
	@Transactional
	public void getAllRoles() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.LEER.name()))
				.thenReturn(mockOperacion());
		Mockito.when(rolService.findByUsuario(ADMIN_LOGIN)).thenReturn(mockRoles(mockRol(ROLE_ADMIN)));

		restUserMockMvc
				.perform(get("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].codigo").value(ROLE_ADMIN))
				.andExpect(jsonPath("$[1].codigo").value("USER"));
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void getAllRolesWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.LEER.name()))
				.thenReturn(mockOperacion());

		restUserMockMvc.perform(
				get("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8).contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = ROLE_ADMIN, username = ADMIN_LOGIN)
	@Transactional
	public void getRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.LEER.name()))
				.thenReturn(mockOperacion());
		Mockito.when(rolService.findByUsuario(ADMIN_LOGIN)).thenReturn(mockRoles(mockRol(ROLE_ADMIN)));

		restUserMockMvc
				.perform(get("/api/roles/" + ROLE_ADMIN).accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.codigo").value(ROLE_ADMIN));
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void getRolWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.LEER.name()))
				.thenReturn(mockOperacion());

		restUserMockMvc.perform(get("/api/roles/" + ROLE_ADMIN).accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = ROLE_ADMIN, username = ADMIN_LOGIN)
	@Transactional
	public void createRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.CREAR.name()))
				.thenReturn(mockOperacion());
		Mockito.when(rolService.findByUsuario(ADMIN_LOGIN)).thenReturn(mockRoles(mockRol(ROLE_ADMIN)));

		restUserMockMvc.perform(post("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isCreated());

		boolean created = rolService.findAll(null).getContent().stream()
				.anyMatch(rol -> rol.getCodigo().equals(TEST_ROL.toString()));
		assertThat(created).isTrue();
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void createRolWithoutPermission() throws Exception {
		restUserMockMvc.perform(post("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = ROLE_ADMIN, username = ADMIN_LOGIN)
	@Transactional
	public void updateRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.EDITAR.name()))
				.thenReturn(mockOperacion());
		Mockito.when(rolService.findByUsuario(ADMIN_LOGIN)).thenReturn(mockRoles(mockRol(ROLE_ADMIN)));

		Rol rol = mockRol(TEST_ROL);
		em.persist(rol);
		RolDTO rolDTO = rolMapper.toDto(rol);
		rolDTO.setNombre("UPDATED_NAME");
		restUserMockMvc
				.perform(put("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(rolDTO)))
				.andExpect(status().isOk());

		boolean updated = rolService.findAll(null).getContent().stream()
				.anyMatch(r -> r.getNombre().equals("UPDATED_NAME"));
		assertThat(updated).isTrue();
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void updateRolWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.EDITAR.name()))
				.thenReturn(mockOperacion());

		restUserMockMvc.perform(put("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = ROLE_ADMIN, username = ADMIN_LOGIN)
	@Transactional
	public void deleteRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.ELIMINAR.name()))
				.thenReturn(mockOperacion());
		Mockito.when(rolService.findByUsuario(ADMIN_LOGIN)).thenReturn(mockRoles(mockRol(ROLE_ADMIN)));

		int preCreateRoles = rolService.findAll(null).getContent().size();
		Rol rol = mockRol(TEST_ROL);
		rolRepository.saveAndFlush(rol);

		int preDeleteRoles = rolService.findAll(null).getContent().size();
		assertThat(preDeleteRoles).isEqualTo(preCreateRoles + 1);

		restUserMockMvc.perform(delete("/api/roles/" + TEST_ROL).accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isOk());

		int afterDeleteRoles = rolService.findAll(null).getContent().size();

		assertThat(afterDeleteRoles).isEqualTo(preDeleteRoles - 1);
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void deleteRolWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion(SUJETO_ROL, TipoAccionOperacion.ELIMINAR.name()))
				.thenReturn(mockOperacion());

		restUserMockMvc.perform(delete("/api/roles/" + TEST_ROL).accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isForbidden());
	}

}
