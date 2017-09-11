package es.tenerife.secretaria.libro.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import es.tenerife.secretaria.libro.SecretariaLibroApp;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.service.RolService;
import es.tenerife.secretaria.libro.web.rest.errors.ExceptionTranslator;
import es.tenerife.secretaria.libro.web.rest.mapper.RolMapper;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecretariaLibroApp.class)
public class RolResourceIntTest {

	@Autowired
	private RolService rolService;

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

	private MockMvc restUserMockMvc;

	private Rol rol;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		RolResource rolResource = new RolResource(rolService, rolMapper);
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(rolResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create a User.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which has a required relationship to the User entity.
	 */
	public static Rol createEntity(EntityManager em) {
		return new Rol();
	}

	@Before
	public void initTest() {
		rol = createEntity(em);
	}

	@Test
	@Transactional
	public void getAllRoles() throws Exception {
		restUserMockMvc
				.perform(get("/api/roles").accept(TestUtil.APPLICATION_JSON_UTF8)
						.contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].nombre").value("ROLE_ADMIN"))
				.andExpect(jsonPath("$[1].nombre").value("ROLE_USER"));
	}

}
