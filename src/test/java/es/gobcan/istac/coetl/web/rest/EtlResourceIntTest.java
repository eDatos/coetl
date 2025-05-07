package es.gobcan.istac.coetl.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import es.gobcan.istac.coetl.CoetlApp;
import es.gobcan.istac.coetl.config.audit.AuditEventPublisher;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Etl.Type;
import es.gobcan.istac.coetl.domain.ExternalItem;
import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.enumeration.TipoPlataformaEjecucion;
import es.gobcan.istac.coetl.errors.ExceptionTranslator;
import es.gobcan.istac.coetl.invocation.facade.NotificationRestInternalFacade;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.repository.EtlRepository;
import es.gobcan.istac.coetl.repository.ParameterRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.service.EtlService;
import es.gobcan.istac.coetl.service.ExecutionService;
import es.gobcan.istac.coetl.service.ParameterService;
import es.gobcan.istac.coetl.web.rest.dto.EtlBaseDTO;
import es.gobcan.istac.coetl.web.rest.dto.EtlDTO;
import es.gobcan.istac.coetl.web.rest.dto.ParameterDTO;
import es.gobcan.istac.coetl.web.rest.mapper.ComputationalThreadsMapper;
import es.gobcan.istac.coetl.web.rest.mapper.EtlMapper;
import es.gobcan.istac.coetl.web.rest.mapper.ExecutionMapper;
import es.gobcan.istac.coetl.web.rest.mapper.ParameterMapper;

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
    private static final Type DEFAULT_PENTAHO_TYPE = Type.TRANSFORMATION;
    private static final Type UPDATED_PENTAHO_TYPE = Type.JOB;
    private static final Type DEFAULT_HOP_TYPE = Type.PIPELINE;
    private static final Type UPDATED_HOP_TYPE = Type.WORKFLOW;
    private static final TipoPlataformaEjecucion PENTAHO_PLATFORM = TipoPlataformaEjecucion.PENTAHO;
    private static final TipoPlataformaEjecucion APACHE_HOP_PLATFORM = TipoPlataformaEjecucion.APACHE_HOP;

    private static final String DEFAULT_ETL_PARAMETER_KEY = "DEFAULT_ETL_PARAMETER_KEY";
    private static final String DEFAULT_ETL_PARAMETER_VALUE = "DEFAULT_ETL_PARAMETER_VALUE";
    private static final String UPDATED_ETL_PARAMETER_VALUE = "UPDATED_ETL_PARAMETER_VALUE";
    private static final String DEFAULT_REPOSITORY_VALUE = "https://testing.com/default.git";
    private static final Parameter.Type DEFAULT_ETL_PARAMETER_TYPE = Parameter.Type.MANUAL;
    private static final Parameter.Typology DEFAULT_ETL_PARAMETER_TYPOLOGY = Parameter.Typology.GENERIC;

    @Autowired
    EntityManager entityManager;

    @Autowired
    EtlRepository etlRepository;

    @SpyBean
    EtlService etlService;

    @SpyBean
    EtlMapper etlMapper;

    @SpyBean
    ExecutionService executionService;

    @SpyBean
    ExecutionMapper executionMapper;

    @Mock
    GitService gitService;

    @Autowired
    ParameterRepository parameterRepository;

    @Autowired
    ParameterService parameterServie;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    AuditEventPublisher auditEventPublisher;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private NotificationRestInternalFacade notificationRestInternalFacade;

    private MockMvc restEtlMockMvc;

    @Autowired
    private ComputationalThreadsMapper computationalThreadsMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(gitService.cloneRepository(any(Etl.class))).thenReturn("/path/to/mocking/repository");
        Mockito.when(gitService.replaceRepository(any(Etl.class))).thenReturn("/path/to/mocking/repository");
        EtlResource etlResource = new EtlResource(etlService, etlMapper, executionService, executionMapper, parameterServie, parameterMapper, auditEventPublisher, gitService,
                notificationRestInternalFacade, computationalThreadsMapper);
        this.restEtlMockMvc = MockMvcBuilders.standaloneSetup(etlResource).setCustomArgumentResolvers(pageableArgumentResolver).setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();

    }

    private ExternalItem mockExternalItem() {
        ExternalItem externalItem = new ExternalItem();
        externalItem.setCode("CODEMOCK");
        externalItem.setName("NAMEMOCK");
        externalItem.setUri("http://mock");
        externalItem.setUrn("urn:mock");
        externalItem.setType(TypeExternalArtefactsEnum.CODE);
        return externalItem;
    }

    private Etl mockEntityForPentahoWithoutId() throws IOException, SQLException {
        Etl etl = new Etl();
        etl.setCode(DEFAULT_CODE);
        etl.setName(DEFAULT_NAME);
        etl.setOrganizationInCharge(DEFAULT_ORGANIZATION_IN_CHARGE);
        etl.setFunctionalInCharge(DEFAULT_FUNCTIONAL_IN_CHARGE);
        etl.setTechnicalInCharge(DEFAULT_TECHNICAL_IN_CHARGE);
        etl.setExecutionPlatform(PENTAHO_PLATFORM);
        etl.setType(DEFAULT_PENTAHO_TYPE);
        etl.setUriRepository(DEFAULT_REPOSITORY_VALUE);
        etl.setExternalItem(mockExternalItem());
        return etl;
    }

    private Etl mockEntityForPentaho() throws IOException, SQLException {
        Etl etl = mockEntityForPentahoWithoutId();
        etl.setId(1L);
        return etl;
    }
    
    private Etl mockEntityForHopWithoutId() throws IOException, SQLException {
        Etl etl = new Etl();
        etl.setCode(DEFAULT_CODE);
        etl.setName(DEFAULT_NAME);
        etl.setOrganizationInCharge(DEFAULT_ORGANIZATION_IN_CHARGE);
        etl.setFunctionalInCharge(DEFAULT_FUNCTIONAL_IN_CHARGE);
        etl.setTechnicalInCharge(DEFAULT_TECHNICAL_IN_CHARGE);
        etl.setExecutionPlatform(APACHE_HOP_PLATFORM);
        etl.setType(DEFAULT_HOP_TYPE);
        etl.setUriRepository(DEFAULT_REPOSITORY_VALUE);
        return etl;
    }

    private Etl mockEntityForHop() throws IOException, SQLException {
        Etl etl = mockEntityForHopWithoutId();
        etl.setId(1L);
        return etl;
    }

    private Etl mockDeletedEntity() throws IOException, SQLException {
        Etl etl = mockEntityForPentahoWithoutId();
        etl.setDeletionDate(Instant.now());
        etl.setDeletedBy("TEST_USER");
        return etl;
    }

    private Parameter mockParameterEntityWithoutId(Etl etl) {
        Parameter parameter = new Parameter();
        parameter.setEtl(etl);
        parameter.setKey(DEFAULT_ETL_PARAMETER_KEY);
        parameter.setValue(DEFAULT_ETL_PARAMETER_VALUE);
        parameter.setType(DEFAULT_ETL_PARAMETER_TYPE);
        parameter.setTypology(DEFAULT_ETL_PARAMETER_TYPOLOGY);
        return parameter;
    }

    private Parameter mockParameterEntity(Etl etl) {
        Parameter parameter = mockParameterEntityWithoutId(etl);
        parameter.setId(1L);
        return parameter;
    }

    @Test
    @Transactional
    public void createEtl_isStatusOk() throws IOException, SQLException, Exception {
        EtlDTO createdEtlDTOMocked = etlMapper.toDto(mockEntityForPentahoWithoutId());

        //@formatter:off
        restEtlMockMvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdEtlDTOMocked)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.code").value(createdEtlDTOMocked.getCode()))
            .andExpect(jsonPath("$.name").value(createdEtlDTOMocked.getName()))
            .andExpect(jsonPath("$.purpose").value(is(nullValue())))
            .andExpect(jsonPath("$.organizationInCharge").value(createdEtlDTOMocked.getOrganizationInCharge()))
            .andExpect(jsonPath("$.functionalInCharge").value(createdEtlDTOMocked.getFunctionalInCharge()))
            .andExpect(jsonPath("$.technicalInCharge").value(createdEtlDTOMocked.getTechnicalInCharge()))
            .andExpect(jsonPath("$.executionPlatform").value(createdEtlDTOMocked.getExecutionPlatform().name()))
            .andExpect(jsonPath("$.type").value(createdEtlDTOMocked.getType().name()))
            .andExpect(jsonPath("$.comments").value(is(nullValue())))
            .andExpect(jsonPath("$.executionDescription").value(is(nullValue())))
            .andExpect(jsonPath("$.executionPlanning").value(is(nullValue())))
            .andExpect(jsonPath("$.uriRepository").isNotEmpty())
            .andExpect(jsonPath("$.deletionDate").value(is(nullValue())))
            .andExpect(jsonPath("$.deletedBy").value(is(nullValue())));
        //@formatter:on
    }

    @Test
    @Transactional
    public void createEtl_isStatusBadRequest_ifExistingId() throws IOException, SQLException, Exception {
        Etl createdEtlMocked = mockEntityForPentaho();
        EtlDTO createdEtlDTOMocked = etlMapper.toDto(createdEtlMocked);

        //@formatter:off
        restEtlMockMvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(createdEtlDTOMocked)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-coetl-error", "error.id-existe"))
            .andExpect(header().string("X-coetl-params", "etl"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void updateEtl_isStatusOk() throws IOException, SQLException, Exception {
        Etl updatedEtlMocked = mockEntityForPentaho();
        updatedEtlMocked.setCode(UPDATED_CODE);
        updatedEtlMocked.setName(UPDATED_NAME);
        updatedEtlMocked.setOrganizationInCharge(UPDATED_ORGANIZATION_IN_CHARGE);
        updatedEtlMocked.setFunctionalInCharge(UPDATED_FUNCTIONAL_IN_CHARGE);
        updatedEtlMocked.setTechnicalInCharge(UPDATED_TECHNICAL_IN_CHARGE);
        updatedEtlMocked.setType(UPDATED_PENTAHO_TYPE);

        EtlDTO updatedEtlDTOMocked = etlMapper.toDto(updatedEtlMocked);

        doReturn(updatedEtlMocked).when(etlMapper).toEntity(updatedEtlDTOMocked);

        doReturn(updatedEtlMocked).when(etlService).update(any(Etl.class));

        doReturn(false).when(etlService).goingToChangeRepository(any(EtlDTO.class));

        //@formatter:off
        restEtlMockMvc.perform(put(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtlDTOMocked)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(updatedEtlDTOMocked.getId()))
            .andExpect(jsonPath("$.code").value(updatedEtlDTOMocked.getCode()))
            .andExpect(jsonPath("$.name").value(updatedEtlDTOMocked.getName()))
            .andExpect(jsonPath("$.purpose").value(is(nullValue())))
            .andExpect(jsonPath("$.organizationInCharge").value(updatedEtlDTOMocked.getOrganizationInCharge()))
            .andExpect(jsonPath("$.functionalInCharge").value(updatedEtlDTOMocked.getFunctionalInCharge()))
            .andExpect(jsonPath("$.technicalInCharge").value(updatedEtlDTOMocked.getTechnicalInCharge()))
            .andExpect(jsonPath("$.executionPlatform").value(updatedEtlDTOMocked.getExecutionPlatform().name()))
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
    public void updateEtl_isStatusBadRequest_ifNotExistingId() throws IOException, SQLException, Exception {
        Etl updatedEtlMocked = mockEntityForPentahoWithoutId();
        updatedEtlMocked.setCode(UPDATED_CODE);
        updatedEtlMocked.setName(UPDATED_NAME);
        updatedEtlMocked.setOrganizationInCharge(UPDATED_ORGANIZATION_IN_CHARGE);
        updatedEtlMocked.setFunctionalInCharge(UPDATED_FUNCTIONAL_IN_CHARGE);
        updatedEtlMocked.setTechnicalInCharge(UPDATED_TECHNICAL_IN_CHARGE);
        updatedEtlMocked.setType(UPDATED_PENTAHO_TYPE);

        EtlDTO updatedEtlDTOMocked = etlMapper.toDto(updatedEtlMocked);

        //@formatter:off
        restEtlMockMvc.perform(put(BASE_URI.concat("?isAttachedFileChanged=\"false\""))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtlDTOMocked)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-coetl-error", "error.id-falta"))
            .andExpect(header().string("X-coetl-params", "etl"));
        //@formatter:on
    }
    
    @Test
    @Transactional
    public void updateEtl_isStatusBadRequest_ifTypeNotSupported() throws IOException, SQLException, Exception {
        Etl updatedEtlMocked = mockEntityForPentaho();
        updatedEtlMocked.setCode(UPDATED_CODE);
        updatedEtlMocked.setName(UPDATED_NAME);
        updatedEtlMocked.setOrganizationInCharge(UPDATED_ORGANIZATION_IN_CHARGE);
        updatedEtlMocked.setFunctionalInCharge(UPDATED_FUNCTIONAL_IN_CHARGE);
        updatedEtlMocked.setTechnicalInCharge(UPDATED_TECHNICAL_IN_CHARGE);
        updatedEtlMocked.setType(UPDATED_HOP_TYPE);

        EtlDTO updatedEtlDTOMocked = etlMapper.toDto(updatedEtlMocked);

        doReturn(updatedEtlMocked).when(etlMapper).toEntity(updatedEtlDTOMocked);

        doReturn(false).when(etlService).goingToChangeRepository(any(EtlDTO.class));

        //@formatter:off
        restEtlMockMvc.perform(put(BASE_URI.concat("?isAttachedFileChanged=\"false\""))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtlDTOMocked)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("error.etl.typeNotSupported"));
        //@formatter:on
    }
    
    @Test
    @Transactional
    public void updateEtl_isStatusBadRequest_ifTypeNotSupported2() throws IOException, SQLException, Exception {
        Etl updatedEtlMocked = mockEntityForHop();
        updatedEtlMocked.setCode(UPDATED_CODE);
        updatedEtlMocked.setName(UPDATED_NAME);
        updatedEtlMocked.setOrganizationInCharge(UPDATED_ORGANIZATION_IN_CHARGE);
        updatedEtlMocked.setFunctionalInCharge(UPDATED_FUNCTIONAL_IN_CHARGE);
        updatedEtlMocked.setTechnicalInCharge(UPDATED_TECHNICAL_IN_CHARGE);
        updatedEtlMocked.setType(UPDATED_PENTAHO_TYPE);

        EtlDTO updatedEtlDTOMocked = etlMapper.toDto(updatedEtlMocked);

        doReturn(updatedEtlMocked).when(etlMapper).toEntity(updatedEtlDTOMocked);

        doReturn(false).when(etlService).goingToChangeRepository(any(EtlDTO.class));

        //@formatter:off
        restEtlMockMvc.perform(put(BASE_URI.concat("?isAttachedFileChanged=\"false\""))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtlDTOMocked)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("error.etl.typeNotSupported"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void deleteEtl_isStatusOk() throws IOException, SQLException, Exception {
        Etl etlToDeleteMocked = mockEntityForPentaho();
        EtlDTO etlToDeleteDTOMocked = etlMapper.toDto(etlToDeleteMocked);

        doReturn(etlToDeleteMocked).when(etlService).findOne(etlToDeleteDTOMocked.getId());

        Etl deletedEtlMocked = mockEntityForPentaho();
        deletedEtlMocked.setDeletionDate(Instant.now());
        deletedEtlMocked.setDeletedBy("test");

        doReturn(deletedEtlMocked).when(etlService).delete(any(Etl.class));

        //@formatter:off
        restEtlMockMvc.perform(delete(BASE_URI + "/{idEtl}", etlToDeleteDTOMocked.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.deletionDate").value(TestUtil.sameInstant(deletedEtlMocked.getDeletionDate())))
            .andExpect(jsonPath("$.deletedBy").value(deletedEtlMocked.getDeletedBy()));
        //@formatter:on
    }

    @Test
    @Transactional
    public void deleteEtl_isStatusBadRequest_ifEtlIsAlreadyDeleted() throws IOException, SQLException, Exception {
        Etl currentlyDeletedEtlMocked = mockEntityForPentaho();
        currentlyDeletedEtlMocked.setDeletionDate(Instant.now());
        currentlyDeletedEtlMocked.setDeletedBy("test");
        EtlDTO deletedEtlDTOMocked = etlMapper.toDto(currentlyDeletedEtlMocked);

        doReturn(currentlyDeletedEtlMocked).when(etlService).findOne(deletedEtlDTOMocked.getId());

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/{idEtl}", deletedEtlDTOMocked.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("error.etl.currentlyDeleted"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void findOneEtl_isStatusOk() throws IOException, SQLException, Exception {
        Etl etlMocked = mockEntityForPentaho();

        EtlDTO etlDTOMocked = etlMapper.toDto(etlMocked);

        doReturn(etlMocked).when(etlService).findOne(etlDTOMocked.getId());

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI + "/{idEtl}", etlDTOMocked.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
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
    public void findAllEtl_isStatusOk() throws IOException, SQLException, Exception {
        Etl etlMocked = mockEntityForPentaho();

        EtlBaseDTO etlDTOMocked = etlMapper.toBaseDto(etlMocked, null, null);

        Page<Etl> etlMockPage = new PageImpl<>(new ArrayList<>(Arrays.asList(etlMocked)));
        doReturn(etlMockPage).when(etlService).findAll(any(String.class), any(Boolean.class), any(Pageable.class), any(String.class), any(String.class), any(String.class),
                any(String.class));

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI + "?sort=id,asc").param("lastExecution", "").param("lastExecutionByResult", "")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etlDTOMocked.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(etlDTOMocked.getCode())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(etlDTOMocked.getName())))
            .andExpect(jsonPath("$.[*].organizationInCharge").value(hasItem(etlDTOMocked.getOrganizationInCharge())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(etlDTOMocked.getType().name())))
            .andExpect(jsonPath("$.[*].executionPlanning").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].deletionDate").value(hasItem(is(nullValue()))))
            .andExpect(jsonPath("$.[*].deletedBy").value(hasItem(is(nullValue()))));
        //@formatter:on
    }

    @Test
    @Transactional
    public void createParameter_isStatusOk() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);
        Parameter mockedParameter = mockParameterEntityWithoutId(createdEtl);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(mockedParameter);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), createdEtl.getId(), RequestMethod.POST)
                .file(mockedParameterDTORequestPart)
                )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.key").value(DEFAULT_ETL_PARAMETER_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_ETL_PARAMETER_VALUE))
            .andExpect(jsonPath("$.type").value(DEFAULT_ETL_PARAMETER_TYPE.name()))
            .andExpect(jsonPath("$.etlId").value(createdEtl.getId()))
            .andExpect(jsonPath("$.optLock").value(0));
        //@formatter:on
    }

    @Test
    @Transactional
    public void createParameter_isStatusBadRequest_ifExistingId() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);
        Parameter mockedParameter = mockParameterEntity(createdEtl);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(mockedParameter);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), createdEtl.getId(), RequestMethod.POST)
                .file(mockedParameterDTORequestPart)
                )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-coetl-error", "error.id-existe"))
        .andExpect(header().string("X-coetl-params", "parameter"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void createParameter_isStatusNotFound_ifReferencesAnotherEtl() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Etl anotherMockedEtl = mockEntityForPentahoWithoutId();
        anotherMockedEtl.setCode(UPDATED_CODE);
        Etl anotherCreatedEtl = etlService.create(anotherMockedEtl);

        Parameter mockedParameter = mockParameterEntityWithoutId(createdEtl);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(mockedParameter);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), anotherCreatedEtl.getId(), RequestMethod.POST)
                .file(mockedParameterDTORequestPart)
                )
        .andDo(print())
        .andExpect(status().isNotFound());
        //@formatter:on
    }

    @Test
    @Transactional
    public void createParameter_fail_ifEtlIsDeleted() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockDeletedEntity();
        Etl deletedEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntityWithoutId(deletedEtl);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(mockedParameter);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), deletedEtl.getId(), RequestMethod.POST)
                .file(mockedParameterDTORequestPart)
                )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-coetl-error", "error.entity.deleted"))
        .andExpect(header().string("X-coetl-params", "etl"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void updateParameter_isStatusOk() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(createdParameter);
        mockedParameterDTO.setValue(UPDATED_ETL_PARAMETER_VALUE);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), createdEtl.getId())
                .file(mockedParameterDTORequestPart)
                .with(req -> { req.setMethod("PUT"); return req; })
                )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.key").value(DEFAULT_ETL_PARAMETER_KEY))
            .andExpect(jsonPath("$.value").value(UPDATED_ETL_PARAMETER_VALUE))
            .andExpect(jsonPath("$.type").value(DEFAULT_ETL_PARAMETER_TYPE.name()))
            .andExpect(jsonPath("$.etlId").value(createdEtl.getId()))
            .andExpect(jsonPath("$.optLock").value(1));
        //@formatter:on
    }

    @Test
    @Transactional
    public void updateParameter_isStatusBadRequest_ifNotExistingId() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntityWithoutId(createdEtl);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(mockedParameter);
        mockedParameterDTO.setValue(UPDATED_ETL_PARAMETER_VALUE);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), createdEtl.getId())
                .file(mockedParameterDTORequestPart)
                .with(req -> { req.setMethod("PUT"); return req; }))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-coetl-error", "error.id-falta"))
            .andExpect(header().string("X-coetl-params", "parameter"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void updateParameter_isStatusNotFound_ifReferencesAnotherEtl() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Etl anotherMockedEtl = mockEntityForPentahoWithoutId();
        anotherMockedEtl.setCode(UPDATED_CODE);
        Etl anotherCreatedEtl = etlService.create(anotherMockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(createdParameter);
        mockedParameterDTO.setValue(UPDATED_ETL_PARAMETER_VALUE);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), anotherCreatedEtl.getId())
                .file(mockedParameterDTORequestPart)
                .with(req -> { req.setMethod("PUT"); return req; }))
            .andDo(print())
            .andExpect(status().isNotFound());
        //@formatter:on
    }

    @Test
    @Transactional
    public void updateParameter_isStatusBadRequest_ifEtlIsDeleted() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockDeletedEntity();
        Etl deletedEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntity(deletedEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);
        ParameterDTO mockedParameterDTO = parameterMapper.toDto(createdParameter);
        mockedParameterDTO.setValue(UPDATED_ETL_PARAMETER_VALUE);
        MockMultipartFile mockedParameterDTORequestPart = new MockMultipartFile("parameterDTO", "", "application/json", TestUtil.convertObjectToJsonBytes(mockedParameterDTO));

        //@formatter:off
        restEtlMockMvc.perform(MockMvcRequestBuilders
                .fileUpload(BASE_URI.concat("/{idEtl}").concat("/parameters"), deletedEtl.getId())
                .file(mockedParameterDTORequestPart)
                .with(req -> { req.setMethod("PUT"); return req; }))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(header().string("X-coetl-error", "error.entity.deleted"))
            .andExpect(header().string("X-coetl-params", "etl"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void deleteParameter_isStatusOk() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);

        //@formatter:off
        restEtlMockMvc.perform(delete(BASE_URI.concat("/{idEtl}").concat("/parameters").concat("/{idParameter}"), createdEtl.getId(), createdParameter.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("X-coetl-alert", "coetlApp.parameter.deleted"))
            .andExpect(header().string("X-coetl-params", createdParameter.getId().toString()));
        //@formatter:on
    }

    @Test
    @Transactional
    public void deleteParameter_isStatusBadRequest_ifEtlIsDeleted() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockDeletedEntity();
        Etl createdEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);

        //@formatter:off
        restEtlMockMvc.perform(delete(BASE_URI.concat("/{idEtl}").concat("/parameters").concat("/{idParameter}"), createdEtl.getId(), createdParameter.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(header().string("X-coetl-error", "error.entity.deleted"))
        .andExpect(header().string("X-coetl-params", "etl"));
        //@formatter:on
    }

    @Test
    @Transactional
    public void deleteParameter_isStatusNotFound_ifReferencesAnotherEtl() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockDeletedEntity();
        Etl createdEtl = etlService.create(mockedEtl);

        Etl anotherMockedEtl = mockEntityForPentahoWithoutId();
        anotherMockedEtl.setCode(UPDATED_CODE);
        Etl anotherCreatedEtl = etlService.create(anotherMockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);

        //@formatter:off
        restEtlMockMvc.perform(delete(BASE_URI.concat("/{idEtl}").concat("/parameters").concat("/{idParameter}"), anotherCreatedEtl.getId(), createdParameter.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isNotFound());
        //@formatter:on
    }

    @Test
    @Transactional
    public void getParameter_isStatusOk() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI.concat("/{idEtl}").concat("/parameters").concat("/{idParameter}"), createdEtl.getId(), createdParameter.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(createdParameter.getId()))
            .andExpect(jsonPath("$.key").value(DEFAULT_ETL_PARAMETER_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_ETL_PARAMETER_VALUE))
            .andExpect(jsonPath("$.type").value(DEFAULT_ETL_PARAMETER_TYPE.name()))
            .andExpect(jsonPath("$.etlId").value(createdEtl.getId()))
            .andExpect(jsonPath("$.optLock").value(0));
        //@formatter:on
    }

    @Test
    @Transactional
    public void getParameter_isStatusNotFound_ifReferencesAnotherEtl() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Etl anotherMockedEtl = mockEntityForPentahoWithoutId();
        anotherMockedEtl.setCode(UPDATED_CODE);
        Etl anotherCreatedEtl = etlService.create(anotherMockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI.concat("/{idEtl}").concat("/parameters").concat("/{idParameter}"), anotherCreatedEtl.getId(), createdParameter.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print())
        .andExpect(status().isNotFound());
        //@formatter:on
    }

    @Test
    @Transactional
    public void findParameters_isStatusOk() throws IOException, SQLException, Exception {
        Etl mockedEtl = mockEntityForPentahoWithoutId();
        Etl createdEtl = etlService.create(mockedEtl);

        Parameter mockedParameter = mockParameterEntity(createdEtl);
        Parameter createdParameter = parameterRepository.saveAndFlush(mockedParameter);

        //@formatter:off
        restEtlMockMvc.perform(get(BASE_URI.concat("/{idEtl}").concat("/parameters"), createdEtl.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].id").value(hasItem(createdParameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_ETL_PARAMETER_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_ETL_PARAMETER_VALUE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_ETL_PARAMETER_TYPE.name())))
            .andExpect(jsonPath("$.[*].etlId").value(hasItem(createdEtl.getId().intValue())))
            .andExpect(jsonPath("$.[*].optLock").value(hasItem(0)));
        //@formatter:on
    }

    @Test
    @Transactional
    public void givenParameterValuePassword_whenEncrypt_thenSuccess() throws Exception {
        String value = "password";

        String cipherText = SecurityUtils.passwordEncoder(value);
        String decryptedCipherText = SecurityUtils.passwordDecode(cipherText);
        Assert.assertEquals(value, decryptedCipherText);
    }
}
