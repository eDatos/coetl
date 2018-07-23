package es.gobcan.coetl.web.rest;

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

import es.gobcan.coetl.CoetlApp;
import es.gobcan.coetl.domain.Idioma;
import es.gobcan.coetl.errors.ExceptionTranslator;
import es.gobcan.coetl.repository.IdiomaRepository;
import es.gobcan.coetl.service.IdiomaService;
import es.gobcan.coetl.web.rest.IdiomaResource;
import es.gobcan.coetl.web.rest.dto.IdiomaDTO;
import es.gobcan.coetl.web.rest.mapper.IdiomaMapper;

/**
 * Test class for the IdiomaResource REST controller.
 *
 * @see IdiomaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class IdiomaResourceIntTest {

    private static final String ENDPOINT_URL = "/api/idiomas";

    private static final String DEFAULT_NOMBRE = "Español";

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

    private Idioma newIdioma;

    private Idioma existingIdioma;

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
    public static Idioma createEntity() {
        return new Idioma().nombre(DEFAULT_NOMBRE);
    }

    @Before
    public void initTest() {
        newIdioma = createEntity();
        existingIdioma = createEntity();
        em.persist(existingIdioma);
    }

    @Test
    @Transactional
    public void createIdioma() throws Exception {
        int databaseSizeBeforeCreate = idiomaRepository.findAll().size();

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(newIdioma);
        restIdiomaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isCreated());

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
        newIdioma.setId(1L);
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(newIdioma);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdiomaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = idiomaRepository.findAll().size();
        // set the field null
        newIdioma.setNombre(null);

        // Create the Idioma, which fails.
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(newIdioma);

        restIdiomaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isBadRequest());

        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIdiomas() throws Exception {
        restIdiomaMockMvc.perform(get(ENDPOINT_URL + "?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(existingIdioma.getId().intValue()))).andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getIdioma() throws Exception {
        restIdiomaMockMvc.perform(get(ENDPOINT_URL + "/{id}", existingIdioma.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(existingIdioma.getId().intValue())).andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdioma() throws Exception {
        restIdiomaMockMvc.perform(get(ENDPOINT_URL + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();

        // Update the idioma
        Idioma updatedIdioma = idiomaRepository.findOne(existingIdioma.getId());
        updatedIdioma.nombre("Alemán");
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(updatedIdioma);

        restIdiomaMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(idiomaDTO))).andExpect(status().isOk());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNombre()).isEqualTo(updatedIdioma.getNombre());
    }

    @Test
    @Transactional
    public void deleteIdioma() throws Exception {
        int databaseSizeBeforeDelete = idiomaRepository.findAll().size();

        // Get the idioma
        restIdiomaMockMvc.perform(delete(ENDPOINT_URL + "/{id}", existingIdioma.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
