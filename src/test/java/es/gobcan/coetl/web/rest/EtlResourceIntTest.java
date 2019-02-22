package es.gobcan.coetl.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.gobcan.coetl.CoetlApp;
import es.gobcan.coetl.config.audit.AuditEventPublisher;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Etl.Type;
import es.gobcan.coetl.domain.File;
import es.gobcan.coetl.errors.ExceptionTranslator;
import es.gobcan.coetl.repository.FileRepository;
import es.gobcan.coetl.service.EtlService;
import es.gobcan.coetl.service.ExecutionService;
import es.gobcan.coetl.web.rest.dto.EtlDTO;
import es.gobcan.coetl.web.rest.mapper.EtlMapper;
import es.gobcan.coetl.web.rest.mapper.ExecutionMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class EtlResourceIntTest {

    private static final String BASE_URI = EtlResource.BASE_URI;

    private static final String DEFAULT_CODE = "DEFAULT_CODE";
    private static final String UPDATED_CODE = "UPDATED_CODE";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String DEFAULT_ORGANIZATION_IN_CHARGE = "DEFAULT_ORGANIZATION_IN_CHARGE";
    private static final String UPDATED_ORGANIZATION_IN_CHARGE = "UPDATED_ORGANIZATION_IN_CHARGE";
    private static final String DEFAULT_FUNCTIONAL_IN_CHARGE = "DEFAULT_FUNCTIONAL_IN_CHARGE";
    private static final String UPDATED_FUNCTIONAL_IN_CHARGE = "UPDATED_FUNCTIONAL_IN_CHARGE";
    private static final String DEFAULT_TECHNICAL_IN_CHARGE = "DEFAULT_TECHNICAL_IN_CHARGE";
    private static final String UPDATED_TECHNICAL_IN_CHARGE = "UPDATED_TECHNICAL_IN_CHARGE";
    private static final Type DEFAULT_TYPE = Type.TRANSFORMATION;
    private static final Type UPDATED_TYPE = Type.JOB;

    private static final String PATH_CODE_FILE = "src/main/resources/banner.txt";
    private static final String PATH_DESCRIPTION_FILE = "src/main/resources/config/data-location.properties";

    @Autowired
    EntityManager entityManager;

    @SpyBean
    EtlService etlService;

    @SpyBean
    EtlMapper etlMapper;

    @SpyBean
    ExecutionService executionService;

    @SpyBean
    ExecutionMapper executionMapper;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    AuditEventPublisher auditEventPublisher;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restEtlMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EtlResource etlResource = new EtlResource(etlService, etlMapper, executionService, executionMapper, auditEventPublisher);
        this.restEtlMockMvc = MockMvcBuilders.standaloneSetup(etlResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    private Etl mockEntityWithoutId() throws IOException, SQLException {
        Etl etl = new Etl();
        etl.setCode(DEFAULT_CODE);
        etl.setName(DEFAULT_NAME);
        etl.setOrganizationInCharge(DEFAULT_ORGANIZATION_IN_CHARGE);
        etl.setFunctionalInCharge(DEFAULT_FUNCTIONAL_IN_CHARGE);
        etl.setTechnicalInCharge(DEFAULT_TECHNICAL_IN_CHARGE);
        etl.setType(DEFAULT_TYPE);
        File etlFile = fileRepository.saveAndFlush(FileResourceIntTest.createEntity(PATH_CODE_FILE, entityManager));
        etl.setEtlFile(etlFile);
        File etlDescriptionFile = fileRepository.saveAndFlush(FileResourceIntTest.createEntity(PATH_DESCRIPTION_FILE, entityManager));
        etl.setEtlDescriptionFile(etlDescriptionFile);
        return etl;
    }

    private Etl mockEntity() throws IOException, SQLException {
        Etl etl = new Etl();
        etl.setId(1L);
        etl.setCode(DEFAULT_CODE);
        etl.setName(DEFAULT_NAME);
        etl.setOrganizationInCharge(DEFAULT_ORGANIZATION_IN_CHARGE);
        etl.setFunctionalInCharge(DEFAULT_FUNCTIONAL_IN_CHARGE);
        etl.setTechnicalInCharge(DEFAULT_TECHNICAL_IN_CHARGE);
        etl.setType(DEFAULT_TYPE);
        File etlFile = fileRepository.saveAndFlush(FileResourceIntTest.createEntity(PATH_CODE_FILE, entityManager));
        etl.setEtlFile(etlFile);
        File etlDescriptionFile = fileRepository.saveAndFlush(FileResourceIntTest.createEntity(PATH_DESCRIPTION_FILE, entityManager));
        etl.setEtlDescriptionFile(etlDescriptionFile);
        return etl;
    }

    @Test
    @Transactional
    public void create() throws IOException, SQLException, Exception {
        EtlDTO createdEtlDTOMocked = etlMapper.toDto(mockEntityWithoutId());

        //@formatter:off
        restEtlMockMvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdEtlDTOMocked)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.code").value(createdEtlDTOMocked.getCode()))
            .andExpect(jsonPath("$.name").value(createdEtlDTOMocked.getName()))
            .andExpect(jsonPath("$.purpose").value(is(nullValue())))
            .andExpect(jsonPath("$.organizationInCharge").value(createdEtlDTOMocked.getOrganizationInCharge()))
            .andExpect(jsonPath("$.functionalInCharge").value(createdEtlDTOMocked.getFunctionalInCharge()))
            .andExpect(jsonPath("$.technicalInCharge").value(createdEtlDTOMocked.getTechnicalInCharge()))
            .andExpect(jsonPath("$.type").value(createdEtlDTOMocked.getType().name()))
            .andExpect(jsonPath("$.comments").value(is(nullValue())))
            .andExpect(jsonPath("$.executionDescription").value(is(nullValue())))
            .andExpect(jsonPath("$.executionPlanning").value(is(nullValue())))
            .andExpect(jsonPath("$.etlFile").isNotEmpty())
            .andExpect(jsonPath("$.etlDescriptionFile").isNotEmpty())
            .andExpect(jsonPath("$.deletionDate").value(is(nullValue())))
            .andExpect(jsonPath("$.deletedBy").value(is(nullValue())));
        //@formatter:on
    }

    @Test
    @Transactional
    public void createWithExistingId() throws IOException, SQLException, Exception {
        Etl createdEtlMocked = mockEntity();
        EtlDTO createdEtlDTOMocked = etlMapper.toDto(createdEtlMocked);

        //@formatter:off
        restEtlMockMvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdEtlDTOMocked)))
            .andExpect(status().isBadRequest());
        //@formatter:on
    }

    @Test
    @Transactional
    public void update() throws IOException, SQLException, Exception {
        Etl updatedEtlMocked = mockEntity();
        updatedEtlMocked.setCode(UPDATED_CODE);
        updatedEtlMocked.setName(UPDATED_NAME);
        updatedEtlMocked.setOrganizationInCharge(UPDATED_ORGANIZATION_IN_CHARGE);
        updatedEtlMocked.setFunctionalInCharge(UPDATED_FUNCTIONAL_IN_CHARGE);
        updatedEtlMocked.setTechnicalInCharge(UPDATED_TECHNICAL_IN_CHARGE);
        updatedEtlMocked.setType(UPDATED_TYPE);

        EtlDTO updatedEtlDTOMocked = etlMapper.toDto(updatedEtlMocked);

        doReturn(updatedEtlMocked).when(etlMapper).toEntity(updatedEtlDTOMocked);

        doReturn(updatedEtlMocked).when(etlService).update(any(Etl.class));

        //@formatter:off
        restEtlMockMvc.perform(put(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtlDTOMocked)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(updatedEtlDTOMocked.getId()))
            .andExpect(jsonPath("$.code").value(updatedEtlDTOMocked.getCode()))
            .andExpect(jsonPath("$.name").value(updatedEtlDTOMocked.getName()))
            .andExpect(jsonPath("$.purpose").value(is(nullValue())))
            .andExpect(jsonPath("$.organizationInCharge").value(updatedEtlDTOMocked.getOrganizationInCharge()))
            .andExpect(jsonPath("$.functionalInCharge").value(updatedEtlDTOMocked.getFunctionalInCharge()))
            .andExpect(jsonPath("$.technicalInCharge").value(updatedEtlDTOMocked.getTechnicalInCharge()))
            .andExpect(jsonPath("$.type").value(updatedEtlDTOMocked.getType().name()))
            .andExpect(jsonPath("$.comments").value(is(nullValue())))
            .andExpect(jsonPath("$.executionDescription").value(is(nullValue())))
            .andExpect(jsonPath("$.executionPlanning").value(is(nullValue())))
            .andExpect(jsonPath("$.deletionDate").value(is(nullValue())))
            .andExpect(jsonPath("$.deletedBy").value(is(nullValue())));
        //@formatter:on
    }

    @Test
    @Transactional
    public void updateWithoutId() throws IOException, SQLException, Exception {
        Etl updatedEtlMocked = mockEntityWithoutId();
        updatedEtlMocked.setCode(UPDATED_CODE);
        updatedEtlMocked.setName(UPDATED_NAME);
        updatedEtlMocked.setOrganizationInCharge(UPDATED_ORGANIZATION_IN_CHARGE);
        updatedEtlMocked.setFunctionalInCharge(UPDATED_FUNCTIONAL_IN_CHARGE);
        updatedEtlMocked.setTechnicalInCharge(UPDATED_TECHNICAL_IN_CHARGE);
        updatedEtlMocked.setType(UPDATED_TYPE);

        EtlDTO updatedEtlDTOMocked = etlMapper.toDto(updatedEtlMocked);

        //@formatter:off
        restEtlMockMvc.perform(put(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtlDTOMocked)))
            .andExpect(status().isBadRequest());
        //@formatter:on
    }

    @Test
    @Transactional
    public void delete() throws IOException, SQLException, Exception {
        Etl etlToDeleteMocked = mockEntity();
        EtlDTO etlToDeleteDTOMocked = etlMapper.toDto(etlToDeleteMocked);

        doReturn(etlToDeleteMocked).when(etlService).findOne(etlToDeleteDTOMocked.getId());

        Etl deletedEtlMocked = mockEntity();
        deletedEtlMocked.setDeletionDate(Instant.now());
        deletedEtlMocked.setDeletedBy("test");

        doReturn(deletedEtlMocked).when(etlService).delete(any(Etl.class));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/{idEtl}", etlToDeleteDTOMocked.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.deletionDate").value(TestUtil.sameInstant(deletedEtlMocked.getDeletionDate())))
            .andExpect(jsonPath("$.deletedBy").value(deletedEtlMocked.getDeletedBy()));
        //@formatter:on
    }

    @Test
    @Transactional
    public void deleteCurrentlyDeleted() throws IOException, SQLException, Exception {
        Etl currentlyDeletedEtlMocked = mockEntity();
        currentlyDeletedEtlMocked.setDeletionDate(Instant.now());
        currentlyDeletedEtlMocked.setDeletedBy("test");
        EtlDTO deletedEtlDTOMocked = etlMapper.toDto(currentlyDeletedEtlMocked);

        doReturn(currentlyDeletedEtlMocked).when(etlService).findOne(deletedEtlDTOMocked.getId());

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/{idEtl}", deletedEtlDTOMocked.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
        //@formatter:on
    }

    @Test
    @Transactional
    public void findOne() throws IOException, SQLException, Exception {
        Etl etlMocked = mockEntity();

        EtlDTO etlDTOMocked = etlMapper.toDto(etlMocked);

        doReturn(etlMocked).when(etlService).findOne(etlDTOMocked.getId());

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI + "/{idEtl}", etlDTOMocked.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(etlDTOMocked.getId()))
            .andExpect(jsonPath("$.code").value(etlDTOMocked.getCode()))
            .andExpect(jsonPath("$.name").value(etlDTOMocked.getName()))
            .andExpect(jsonPath("$.purpose").value(is(nullValue())))
            .andExpect(jsonPath("$.organizationInCharge").value(etlDTOMocked.getOrganizationInCharge()))
            .andExpect(jsonPath("$.functionalInCharge").value(etlDTOMocked.getFunctionalInCharge()))
            .andExpect(jsonPath("$.technicalInCharge").value(etlDTOMocked.getTechnicalInCharge()))
            .andExpect(jsonPath("$.type").value(etlDTOMocked.getType().name()))
            .andExpect(jsonPath("$.comments").value(is(nullValue())))
            .andExpect(jsonPath("$.executionDescription").value(is(nullValue())))
            .andExpect(jsonPath("$.executionPlanning").value(is(nullValue())))
            .andExpect(jsonPath("$.deletionDate").value(is(nullValue())))
            .andExpect(jsonPath("$.deletedBy").value(is(nullValue())));
        //@formatter:on
    }

    @Test
    @Transactional
    public void findAll() throws IOException, SQLException, Exception {
        Etl etlMocked = mockEntity();

        EtlDTO etlDTOMocked = etlMapper.toDto(etlMocked);

        Page<Etl> etlMockPage = new PageImpl<>(new ArrayList<>(Arrays.asList(etlMocked)));
        doReturn(etlMockPage).when(etlService).findAll(any(String.class), any(Boolean.class), any(Pageable.class));

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI + "?sort=id,asc")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etlDTOMocked.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(etlDTOMocked.getCode())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(etlDTOMocked.getName())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].organizationInCharge").value(hasItem(etlDTOMocked.getOrganizationInCharge())))
            .andExpect(jsonPath("$.[*].functionalInCharge").value(hasItem(etlDTOMocked.getFunctionalInCharge())))
            .andExpect(jsonPath("$.[*].technicalInCharge").value(hasItem(etlDTOMocked.getTechnicalInCharge())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(etlDTOMocked.getType().name())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].executionDescription").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].executionPlanning").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].deletionDate").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].deletedBy").value(hasItem(is(nullValue()))));
        //@formatter:on
    }
}
