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
import com.arte.application.template.domain.Idioma;
import com.arte.application.template.repository.IdiomaRepository;
import com.arte.application.template.service.IdiomaService;
import com.arte.application.template.web.rest.dto.IdiomaDTO;
import com.arte.application.template.web.rest.errors.ExceptionTranslator;
import com.arte.application.template.web.rest.mapper.IdiomaMapper;

/**
 * Test class for the IdiomaResource REST controller.
 *
 * @see IdiomaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
public class IdiomaResourceIntTest {

    private static final String BASE_URL = "/api/idiomas";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private IdiomaRepository idiomaRepository;

    @Autowired
    private IdiomaMapper idiomaMapper;

    @Autowired
    private IdiomaService idiomaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIdiomaMockMvc;

    private Idioma idioma;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IdiomaResource idiomaResource = new IdiomaResource(idiomaService, idiomaMapper);
        this.restIdiomaMockMvc = MockMvcBuilders.standaloneSetup(idiomaResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Idioma createEntity(EntityManager em) {
        Idioma idioma = new Idioma().nombre(DEFAULT_NOMBRE);
        return idioma;
    }

    @Before
    public void initTest() {
        idioma = createEntity(em);
    }

    @Test
    @Transactional
    public void createIdioma() throws Exception {
        int databaseSizeBeforeCreate = idiomaRepository.findAll().size();

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);
        restIdiomaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isCreated());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeCreate + 1);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createIdiomaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = idiomaRepository.findAll().size();

        // Create the Idioma with an existing ID
        idioma.setId(1L);
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdiomaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = idiomaRepository.findAll().size();
        // set the field null
        idioma.setNombre(null);

        // Create the Idioma, which fails.
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        restIdiomaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isBadRequest());

        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIdiomas() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList
        restIdiomaMockMvc.perform(get(BASE_URL + "?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(idioma.getId().intValue()))).andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getIdioma() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get the idioma
        restIdiomaMockMvc.perform(get(BASE_URL + "/{id}", idioma.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(idioma.getId().intValue())).andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdioma() throws Exception {
        // Get the idioma
        restIdiomaMockMvc.perform(get(BASE_URL + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdioma() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();

        // Update the idioma
        Idioma updatedIdioma = idiomaRepository.findOne(idioma.getId());
        updatedIdioma.nombre(UPDATED_NOMBRE);
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(updatedIdioma);

        restIdiomaMockMvc.perform(put(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isOk());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIdiomaMockMvc.perform(put(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isCreated());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIdioma() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);
        int databaseSizeBeforeDelete = idiomaRepository.findAll().size();

        // Get the idioma
        restIdiomaMockMvc.perform(delete(BASE_URL + "/{id}", idioma.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Idioma.class);
        Idioma idioma1 = new Idioma();
        idioma1.setId(1L);
        Idioma idioma2 = new Idioma();
        idioma2.setId(idioma1.getId());
        assertThat(idioma1).isEqualTo(idioma2);
        idioma2.setId(2L);
        assertThat(idioma1).isNotEqualTo(idioma2);
        idioma1.setId(null);
        assertThat(idioma1).isNotEqualTo(idioma2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdiomaDTO.class);
        IdiomaDTO idiomaDTO1 = new IdiomaDTO();
        idiomaDTO1.setId(1L);
        IdiomaDTO idiomaDTO2 = new IdiomaDTO();
        assertThat(idiomaDTO1).isNotEqualTo(idiomaDTO2);
        idiomaDTO2.setId(idiomaDTO1.getId());
        assertThat(idiomaDTO1).isEqualTo(idiomaDTO2);
        idiomaDTO2.setId(2L);
        assertThat(idiomaDTO1).isNotEqualTo(idiomaDTO2);
        idiomaDTO1.setId(null);
        assertThat(idiomaDTO1).isNotEqualTo(idiomaDTO2);
    }
}
