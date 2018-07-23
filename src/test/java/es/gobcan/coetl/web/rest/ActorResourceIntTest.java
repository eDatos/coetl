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
import es.gobcan.coetl.domain.Actor;
import es.gobcan.coetl.errors.ExceptionTranslator;
import es.gobcan.coetl.repository.ActorRepository;
import es.gobcan.coetl.service.ActorService;
import es.gobcan.coetl.service.DocumentoService;
import es.gobcan.coetl.web.rest.ActorResource;
import es.gobcan.coetl.web.rest.dto.ActorDTO;
import es.gobcan.coetl.web.rest.mapper.ActorMapper;

/**
 * Test class for the ActorResource REST controller.
 *
 * @see ActorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class ActorResourceIntTest {

    private static final String ENDPOINT_URL = "/api/actores";

    private static final String DEFAULT_NOMBRE = "Juan";
    private static final String DEFAULT_APELLIDO_1 = "Rodríguez";
    private static final String DEFAULT_APELLIDO_2 = "Fernández";

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private ActorService actorService;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActorMockMvc;

    private Actor newActor;

    private Actor existingActor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActorResource actorResource = new ActorResource(actorService, actorMapper, documentoService);
        this.restActorMockMvc = MockMvcBuilders.standaloneSetup(actorResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createEntity() {
        Actor actor = new Actor();
        actor.setNombre(DEFAULT_NOMBRE);
        actor.setApellido1(DEFAULT_APELLIDO_1);
        actor.setApellido2(DEFAULT_APELLIDO_2);
        return actor;
    }

    @Before
    public void initTest() {
        newActor = createEntity();
        existingActor = createEntity();
        em.persist(existingActor);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(newActor);
        restActorMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(actorDTO))).andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testActor.getApellido1()).isEqualTo(DEFAULT_APELLIDO_1);
        assertThat(testActor.getApellido2()).isEqualTo(DEFAULT_APELLIDO_2);
    }

    @Test
    @Transactional
    public void createActorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor with an existing ID
        newActor.setId(1L);
        ActorDTO actorDTO = actorMapper.toDto(newActor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(actorDTO))).andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        newActor.setNombre(null);

        // Create the Actor, which fails.
        ActorDTO actorDTO = actorMapper.toDto(newActor);

        restActorMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(actorDTO))).andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellido1IsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        newActor.setApellido1(null);

        // Create the Actor, which fails.
        ActorDTO actorDTO = actorMapper.toDto(newActor);

        restActorMockMvc.perform(post(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(actorDTO))).andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActors() throws Exception {
        restActorMockMvc.perform(get(ENDPOINT_URL + "?sort=id,desc")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(existingActor.getId().intValue()))).andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].apellido1").value(hasItem(DEFAULT_APELLIDO_1.toString()))).andExpect(jsonPath("$.[*].apellido2").value(hasItem(DEFAULT_APELLIDO_2.toString())));
    }

    @Test
    @Transactional
    public void getActor() throws Exception {
        restActorMockMvc.perform(get(ENDPOINT_URL + "/{id}", existingActor.getId())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(existingActor.getId().intValue())).andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
                .andExpect(jsonPath("$.apellido1").value(DEFAULT_APELLIDO_1.toString())).andExpect(jsonPath("$.apellido2").value(DEFAULT_APELLIDO_2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get(ENDPOINT_URL + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findOne(existingActor.getId());
        updatedActor.setNombre("María");
        updatedActor.setApellido1("Pérez");
        updatedActor.setApellido2("Hernández");
        ActorDTO actorDTO = actorMapper.toDto(updatedActor);

        restActorMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(actorDTO))).andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getNombre()).isEqualTo(updatedActor.getNombre());
        assertThat(testActor.getApellido1()).isEqualTo(updatedActor.getApellido1());
        assertThat(testActor.getApellido2()).isEqualTo(updatedActor.getApellido2());
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Get the actor
        restActorMockMvc.perform(delete(ENDPOINT_URL + "/{id}", existingActor.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
