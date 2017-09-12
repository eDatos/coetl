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
import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.service.RolService;
import es.tenerife.secretaria.libro.web.rest.dto.RolDTO;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class RolResourceIntTest {

	private static final String TEST_ROL = "TEST_ROL";

	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	private static final String DEFAULT_LOGIN = "testLoginName";

	@Autowired
	private EntityManager em;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private RolService rolService;

	@MockBean
	private OperacionService operacionService;

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
		rol.setNombre(ROLE_ADMIN);
		return rol;

	}

	private RolDTO createRolDTO() {
		RolDTO rolDTO = new RolDTO();
		rolDTO.setNombre(TEST_ROL);
		return rolDTO;
	}

	@Test
	@WithMockUser(roles = "ADMIN", username = DEFAULT_LOGIN)
	@Transactional
	public void getAllRoles() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "LEER")).thenReturn(mockOperacion());
		restUserMockMvc
				.perform(get("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].nombre").value(ROLE_ADMIN))
				.andExpect(jsonPath("$[1].nombre").value("ROLE_USER"));
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void getAllRolesWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "LEER")).thenReturn(mockOperacion());

		restUserMockMvc.perform(
				get("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8).contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN", username = DEFAULT_LOGIN)
	@Transactional
	public void getRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "LEER")).thenReturn(mockOperacion());

		restUserMockMvc
				.perform(get("/api/roles/" + ROLE_ADMIN).accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.nombre").value(ROLE_ADMIN));
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void getRolWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "LEER")).thenReturn(mockOperacion());

		restUserMockMvc.perform(get("/api/roles/" + ROLE_ADMIN).accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN", username = DEFAULT_LOGIN)
	@Transactional
	public void createRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "CREAR")).thenReturn(mockOperacion());

		restUserMockMvc.perform(post("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isCreated());

		boolean created = rolService.findAll(null).getContent().stream()
				.anyMatch(rol -> rol.getNombre().equals(TEST_ROL.toString()));
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
	@WithMockUser(roles = "ADMIN", username = DEFAULT_LOGIN)
	@Transactional
	public void updateRol() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "EDITAR")).thenReturn(mockOperacion());

		RolDTO createRolDTO = createRolDTO();
		// createRolDTO.setNombre(nombre);
		restUserMockMvc.perform(post("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO)))
				.andExpect(status().isCreated());

		boolean created = rolService.findAll(null).getContent().stream()
				.anyMatch(rol -> rol.getNombre().equals(TEST_ROL.toString()));
		assertThat(created).isTrue();
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void updateRolWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "EDITAR")).thenReturn(mockOperacion());

		restUserMockMvc.perform(put("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))

				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "", username = DEFAULT_LOGIN)
	@Transactional
	public void deleteRolWithoutPermission() throws Exception {
		Mockito.when(operacionService.findBySujetoAndAccion("ROL", "ELIMINAR")).thenReturn(mockOperacion());

		restUserMockMvc.perform(delete("/api/roles/" + TEST_ROL).accept(TestUtil.APPLICATION_JSON_UTF8)
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(createRolDTO())))
				.andExpect(status().isForbidden());
	}

}
