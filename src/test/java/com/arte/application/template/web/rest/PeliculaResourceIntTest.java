package com.arte.application.template.web.rest;

import static com.arte.application.template.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
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
import com.arte.application.template.domain.Actor;
import com.arte.application.template.domain.Categoria;
import com.arte.application.template.domain.Pelicula;
import com.arte.application.template.repository.PeliculaRepository;
import com.arte.application.template.service.ActorService;
import com.arte.application.template.service.CategoriaService;
import com.arte.application.template.service.DocumentoService;
import com.arte.application.template.service.PeliculaService;
import com.arte.application.template.web.rest.dto.PeliculaDTO;
import com.arte.application.template.web.rest.errors.ExceptionTranslator;
import com.arte.application.template.web.rest.mapper.PeliculaMapper;

/**
 * Test class for the PeliculaResource REST controller.
 *
 * @see PeliculaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArteApplicationTemplateApp.class)
public class PeliculaResourceIntTest {

    private static final String BASE_URL = "/api/peliculas";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_ESTRENO = ZonedDateTime.now();
    private static final ZonedDateTime UPDATED_FECHA_ESTRENO = ZonedDateTime.now().plusDays(1L);

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private PeliculaMapper peliculaMapper;

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeliculaMockMvc;

    private Pelicula pelicula;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PeliculaResource peliculaResource = new PeliculaResource(peliculaService, peliculaMapper, documentoService);
        this.restPeliculaMockMvc = MockMvcBuilders.standaloneSetup(peliculaResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pelicula createEntity(EntityManager em) {
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(DEFAULT_TITULO);
        pelicula.setDescripcion(DEFAULT_DESCRIPCION);
        pelicula.setFechaEstreno(DEFAULT_FECHA_ESTRENO);
        return pelicula;
    }

    @Before
    public void initTest() {
        pelicula = createEntity(em);
        Categoria categoria = CategoriaResourceIntTest.createEntity(em);
        categoriaService.save(categoria);

        Actor actor = ActorResourceIntTest.createEntity(em);
        actorService.save(actor);

        pelicula.addCategoria(categoria);
        pelicula.addActor(actor);
    }

    @Test
    @Transactional
    public void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);
        restPeliculaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate + 1);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPelicula.getFechaEstreno()).isEqualTo(DEFAULT_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    public void createPeliculaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula with an existing ID
        pelicula.setId(1L);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeliculaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setTitulo(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        restPeliculaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setDescripcion(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        restPeliculaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnoestrenoIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        pelicula.setFechaEstreno(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        restPeliculaMockMvc.perform(post(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeliculas() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get all the peliculaList
        restPeliculaMockMvc.perform(get(BASE_URL + "?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pelicula.getId().intValue()))).andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString()))).andExpect(jsonPath("$.[*].fechaEstreno").value(hasItem(sameInstant(DEFAULT_FECHA_ESTRENO))));
    }

    @Test
    @Transactional
    public void getPelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);

        // Get the pelicula
        restPeliculaMockMvc.perform(get(BASE_URL + "/{id}", pelicula.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(pelicula.getId().intValue())).andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
                .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString())).andExpect(jsonPath("$.fechaEstreno").value(sameInstant(DEFAULT_FECHA_ESTRENO)));
    }

    @Test
    @Transactional
    public void getNonExistingPelicula() throws Exception {
        // Get the pelicula
        restPeliculaMockMvc.perform(get(BASE_URL + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findOne(pelicula.getId());

        PeliculaDTO peliculaDTO = peliculaMapper.toDto(updatedPelicula);
        peliculaDTO.setTitulo(UPDATED_TITULO);
        peliculaDTO.setDescripcion(UPDATED_DESCRIPCION);
        peliculaDTO.setFechaEstreno(UPDATED_FECHA_ESTRENO);

        restPeliculaMockMvc.perform(put(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testPelicula.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPelicula.getFechaEstreno()).isEqualTo(UPDATED_FECHA_ESTRENO);
    }

    @Test
    @Transactional
    public void updateNonExistingPelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(pelicula);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPeliculaMockMvc.perform(put(BASE_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isCreated());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePelicula() throws Exception {
        // Initialize the database
        peliculaRepository.saveAndFlush(pelicula);
        int databaseSizeBeforeDelete = peliculaRepository.findAll().size();

        // Get the pelicula
        restPeliculaMockMvc.perform(delete(BASE_URL + "/{id}", pelicula.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pelicula.class);
        Pelicula pelicula1 = new Pelicula();
        pelicula1.setId(1L);
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setId(pelicula1.getId());
        assertThat(pelicula1).isEqualTo(pelicula2);
        pelicula2.setId(2L);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
        pelicula1.setId(null);
        assertThat(pelicula1).isNotEqualTo(pelicula2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeliculaDTO.class);
        PeliculaDTO peliculaDTO1 = new PeliculaDTO();
        peliculaDTO1.setId(1L);
        PeliculaDTO peliculaDTO2 = new PeliculaDTO();
        assertThat(peliculaDTO1).isNotEqualTo(peliculaDTO2);
        peliculaDTO2.setId(peliculaDTO1.getId());
        assertThat(peliculaDTO1).isEqualTo(peliculaDTO2);
        peliculaDTO2.setId(2L);
        assertThat(peliculaDTO1).isNotEqualTo(peliculaDTO2);
        peliculaDTO1.setId(null);
        assertThat(peliculaDTO1).isNotEqualTo(peliculaDTO2);
    }
}
