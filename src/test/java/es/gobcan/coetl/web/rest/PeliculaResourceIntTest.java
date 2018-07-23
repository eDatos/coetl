package es.gobcan.coetl.web.rest;

import static es.gobcan.coetl.web.rest.TestUtil.sameInstant;
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

import es.gobcan.coetl.CoetlApp;
import es.gobcan.coetl.domain.Actor;
import es.gobcan.coetl.domain.Categoria;
import es.gobcan.coetl.domain.Pelicula;
import es.gobcan.coetl.errors.ExceptionTranslator;
import es.gobcan.coetl.repository.PeliculaRepository;
import es.gobcan.coetl.service.ActorService;
import es.gobcan.coetl.service.CategoriaService;
import es.gobcan.coetl.service.DocumentoService;
import es.gobcan.coetl.service.PeliculaService;
import es.gobcan.coetl.web.rest.PeliculaResource;
import es.gobcan.coetl.web.rest.dto.PeliculaDTO;
import es.gobcan.coetl.web.rest.mapper.PeliculaMapper;

/**
 * Test class for the PeliculaResource REST controller.
 *
 * @see PeliculaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class PeliculaResourceIntTest {

    private static final String ENDPOINT_URL = "/api/peliculas";

    private static final String DEFAULT_TITULO = "Los puentes de Madison";
    private static final String DEFAULT_DESCRIPCION = "Descripción de la película";
    private static final ZonedDateTime DEFAULT_FECHA_ESTRENO = ZonedDateTime.now();

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

    private Pelicula newPelicula;

    private Pelicula existingPelicula;

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
    public static Pelicula createEntity() {
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(DEFAULT_TITULO);
        pelicula.setDescripcion(DEFAULT_DESCRIPCION);
        pelicula.setFechaEstreno(DEFAULT_FECHA_ESTRENO);
        return pelicula;
    }

    @Before
    public void initTest() {
        newPelicula = createEntity();
        existingPelicula = createEntity();
        em.persist(existingPelicula);

        Categoria categoria = CategoriaResourceIntTest.createEntity();
        categoriaService.save(categoria);

        Actor actor = ActorResourceIntTest.createEntity();
        actorService.save(actor);

        newPelicula.addCategoria(categoria);
        newPelicula.addActor(actor);
    }

    @Test
    @Transactional
    public void createPelicula() throws Exception {
        int databaseSizeBeforeCreate = peliculaRepository.findAll().size();

        // Create the Pelicula
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(newPelicula);
        restPeliculaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isCreated());

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
        newPelicula.setId(1L);
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(newPelicula);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeliculaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        newPelicula.setTitulo(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(newPelicula);

        restPeliculaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        newPelicula.setDescripcion(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(newPelicula);

        restPeliculaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnoestrenoIsRequired() throws Exception {
        int databaseSizeBeforeTest = peliculaRepository.findAll().size();
        // set the field null
        newPelicula.setFechaEstreno(null);

        // Create the Pelicula, which fails.
        PeliculaDTO peliculaDTO = peliculaMapper.toDto(newPelicula);

        restPeliculaMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isBadRequest());

        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeliculas() throws Exception {

        // Get all the peliculaList
        restPeliculaMockMvc.perform(get(ENDPOINT_URL + "?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(existingPelicula.getId().intValue()))).andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString()))).andExpect(jsonPath("$.[*].fechaEstreno").value(hasItem(sameInstant(DEFAULT_FECHA_ESTRENO))));
    }

    @Test
    @Transactional
    public void getPelicula() throws Exception {
        // Get the pelicula
        restPeliculaMockMvc.perform(get(ENDPOINT_URL + "/{id}", existingPelicula.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(existingPelicula.getId().intValue())).andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()))
                .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString())).andExpect(jsonPath("$.fechaEstreno").value(sameInstant(DEFAULT_FECHA_ESTRENO)));
    }

    @Test
    @Transactional
    public void getNonExistingPelicula() throws Exception {
        restPeliculaMockMvc.perform(get(ENDPOINT_URL + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePelicula() throws Exception {
        int databaseSizeBeforeUpdate = peliculaRepository.findAll().size();

        // Update the pelicula
        Pelicula updatedPelicula = peliculaRepository.findOne(existingPelicula.getId());

        PeliculaDTO peliculaDTO = peliculaMapper.toDto(updatedPelicula);
        peliculaDTO.setTitulo("La guerra de las galaxias");
        peliculaDTO.setDescripcion("...");
        peliculaDTO.setFechaEstreno(ZonedDateTime.now().plusDays(540));

        restPeliculaMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(peliculaDTO))).andExpect(status().isOk());

        // Validate the Pelicula in the database
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeUpdate);
        Pelicula testPelicula = peliculaList.get(peliculaList.size() - 1);
        assertThat(testPelicula.getTitulo()).isEqualTo(peliculaDTO.getTitulo());
        assertThat(testPelicula.getDescripcion()).isEqualTo(peliculaDTO.getDescripcion());
        assertThat(testPelicula.getFechaEstreno()).isEqualTo(peliculaDTO.getFechaEstreno());
    }

    @Test
    @Transactional
    public void deletePelicula() throws Exception {
        int databaseSizeBeforeDelete = peliculaRepository.findAll().size();

        // Get the pelicula
        restPeliculaMockMvc.perform(delete(ENDPOINT_URL + "/{id}", existingPelicula.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Pelicula> peliculaList = peliculaRepository.findAll();
        assertThat(peliculaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
