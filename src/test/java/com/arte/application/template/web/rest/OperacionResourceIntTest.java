package com.arte.application.template.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import com.arte.application.template.ArteApplicationTemplateApp;
import com.arte.application.template.domain.Operacion;
import com.arte.application.template.repository.OperacionRepository;
import com.arte.application.template.service.OperacionService;
import com.arte.application.template.web.rest.OperacionResource;
import com.arte.application.template.web.rest.dto.OperacionDTO;
import com.arte.application.template.web.rest.errors.ExceptionTranslator;
import com.arte.application.template.web.rest.mapper.OperacionMapper;

/**
 * Test class for the OperacionResource REST controller.
 *
 * @see OperacionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
public class OperacionResourceIntTest {

	private static final String DEFAULT_ACCION = "AAAAAAAAAA";
	private static final String UPDATED_ACCION = "BBBBBBBBBB";

	private static final String DEFAULT_SUJETO = "AAAAAAAAAA";
	private static final String UPDATED_SUJETO = "BBBBBBBBBB";

	@Autowired
	private OperacionRepository operacionRepository;

	@Autowired
	private OperacionMapper operacionMapper;

	@Autowired
	private OperacionService operacionService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restOperacionMockMvc;

	private Operacion operacion;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		OperacionResource operacionResource = new OperacionResource(operacionService, operacionMapper);
		this.restOperacionMockMvc = MockMvcBuilders.standaloneSetup(operacionResource)
				.setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static Operacion createEntity(EntityManager em) {
		Operacion operacion = new Operacion().accion(DEFAULT_ACCION).sujeto(DEFAULT_SUJETO);
		return operacion;
	}

	@Before
	public void initTest() {
		operacion = createEntity(em);
	}

	@Test
	@Transactional
	public void createOperacion() throws Exception {
		int databaseSizeBeforeCreate = operacionRepository.findAll().size();

		// Create the Operacion
		OperacionDTO operacionDTO = operacionMapper.toDto(operacion);
		restOperacionMockMvc
				.perform(post("/api/operaciones").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(operacionDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the Operacion in the database
		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void createOperacionWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = operacionRepository.findAll().size();

		// Create the Operacion with an existing ID
		operacion.setId(1L);
		OperacionDTO operacionDTO = operacionMapper.toDto(operacion);

		// An entity with an existing ID cannot be created, so this API call must fail
		restOperacionMockMvc
				.perform(post("/api/operaciones").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(operacionDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the Alice in the database
		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkAccionIsRequired() throws Exception {
		int databaseSizeBeforeTest = operacionRepository.findAll().size();
		// set the field null
		operacion.setAccion(null);

		// Create the Operacion, which fails.
		OperacionDTO operacionDTO = operacionMapper.toDto(operacion);

		restOperacionMockMvc
				.perform(post("/api/operaciones").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(operacionDTO)))
				.andExpect(status().isMethodNotAllowed());

		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkSujetoIsRequired() throws Exception {
		int databaseSizeBeforeTest = operacionRepository.findAll().size();
		// set the field null
		operacion.setSujeto(null);

		// Create the Operacion, which fails.
		OperacionDTO operacionDTO = operacionMapper.toDto(operacion);

		restOperacionMockMvc
				.perform(post("/api/operaciones").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(operacionDTO)))
				.andExpect(status().isMethodNotAllowed());

		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllOperacions() throws Exception {
		// Initialize the database
		operacionRepository.saveAndFlush(operacion);

		// Get all the operacionList
		restOperacionMockMvc.perform(get("/api/operaciones?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(operacion.getId().intValue())))
				.andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION.toString())))
				.andExpect(jsonPath("$.[*].sujeto").value(hasItem(DEFAULT_SUJETO.toString())));
	}

	@Test
	@Transactional
	public void getOperacion() throws Exception {
		// Initialize the database
		operacionRepository.saveAndFlush(operacion);

		// Get the operacion
		restOperacionMockMvc.perform(get("/api/operaciones/{id}", operacion.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(operacion.getId().intValue()))
				.andExpect(jsonPath("$.accion").value(DEFAULT_ACCION.toString()))
				.andExpect(jsonPath("$.sujeto").value(DEFAULT_SUJETO.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingOperacion() throws Exception {
		// Get the operacion
		restOperacionMockMvc.perform(get("/api/operaciones/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateOperacion() throws Exception {
		// Initialize the database
		operacionRepository.saveAndFlush(operacion);
		int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

		// Update the operacion
		Operacion updatedOperacion = operacionRepository.findOne(operacion.getId());
		updatedOperacion.accion(UPDATED_ACCION).sujeto(UPDATED_SUJETO);
		OperacionDTO operacionDTO = operacionMapper.toDto(updatedOperacion);

		restOperacionMockMvc
				.perform(put("/api/operaciones").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(operacionDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the Operacion in the database
		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void updateNonExistingOperacion() throws Exception {
		int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

		// Create the Operacion
		OperacionDTO operacionDTO = operacionMapper.toDto(operacion);

		// If the entity doesn't have an ID, it will be created instead of just being
		// updated
		restOperacionMockMvc
				.perform(put("/api/operaciones").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(operacionDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the Operacion in the database
		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void deleteOperacion() throws Exception {
		// Initialize the database
		operacionRepository.saveAndFlush(operacion);
		int databaseSizeBeforeDelete = operacionRepository.findAll().size();

		// Get the operacion
		restOperacionMockMvc
				.perform(delete("/api/operaciones/{id}", operacion.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isMethodNotAllowed());

		// Validate the database is empty
		List<Operacion> operacionList = operacionRepository.findAll();
		assertThat(operacionList).hasSize(databaseSizeBeforeDelete);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Operacion.class);
		Operacion operacion1 = new Operacion();
		operacion1.setId(1L);
		Operacion operacion2 = new Operacion();
		operacion2.setId(operacion1.getId());
		assertThat(operacion1).isEqualTo(operacion2);
		operacion2.setId(2L);
		assertThat(operacion1).isNotEqualTo(operacion2);
		operacion1.setId(null);
		assertThat(operacion1).isNotEqualTo(operacion2);
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(OperacionDTO.class);
		OperacionDTO operacionDTO1 = new OperacionDTO();
		operacionDTO1.setId(1L);
		OperacionDTO operacionDTO2 = new OperacionDTO();
		assertThat(operacionDTO1).isNotEqualTo(operacionDTO2);
		operacionDTO2.setId(operacionDTO1.getId());
		assertThat(operacionDTO1).isEqualTo(operacionDTO2);
		operacionDTO2.setId(2L);
		assertThat(operacionDTO1).isNotEqualTo(operacionDTO2);
		operacionDTO1.setId(null);
		assertThat(operacionDTO1).isNotEqualTo(operacionDTO2);
	}

}
