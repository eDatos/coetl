package es.gobcan.coetl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.gobcan.coetl.CoetlApp;
import es.gobcan.coetl.domain.File;
import es.gobcan.coetl.errors.ExceptionTranslator;
import es.gobcan.coetl.repository.FileRepository;
import es.gobcan.coetl.service.FileService;
import es.gobcan.coetl.web.rest.mapper.FileMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class FileResourceIntTest {

    private static final String BASE_URI = FileResource.BASE_URI;
    private static final String SLASH = "/";
    private static final String PATH_FILE = "src/main/resources/banner.txt";

    @Autowired
    EntityManager entityManager;

    @Autowired
    FileService fileService;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restFileMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileResource fileResource = new FileResource(fileService, fileMapper);
        this.restFileMockMvc = MockMvcBuilders.standaloneSetup(fileResource).setControllerAdvice(exceptionTranslator).setMessageConverters(jacksonMessageConverter).build();
    }

    public static File createEntity(String pathFile, EntityManager entityManager) throws IOException, SQLException {
        File file = buildFileFromFilePath(pathFile, entityManager);
        return file;
    }

    private static File buildFileFromFilePath(String pathFile, EntityManager entityManager) throws IOException, SQLException {
        Path path = Paths.get(pathFile);
        File file = new File();
        file.setName(path.getFileName().toString());
        file.setDataContentType(Files.probeContentType(path));
        file.setLength(path.toFile().length());
        Blob data = Hibernate.getLobCreator((Session) entityManager.getDelegate()).createBlob(Files.readAllBytes(path));
        file.setData(data);
        return file;
    }

    @Test
    @Transactional
    public void buildFileFromPath() throws IOException, SQLException {
        File file = createEntity(PATH_FILE, entityManager);
        assertThat(file.getName(), is(equalTo("banner.txt")));
    }

    @Test
    @Transactional
    public void create() throws IOException, SQLException, Exception {
        File file = createEntity(PATH_FILE, entityManager);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", file.getName(), file.getDataContentType(), file.getData().getBinaryStream());
        //@formatter:off
        restFileMockMvc.perform(fileUpload(BASE_URI).file(mockMultipartFile))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.name").value(file.getName()))
            .andExpect(jsonPath("$.dataContentType").value(file.getDataContentType()))
            .andExpect(jsonPath("$.length").value(file.getLength()));
        //@formatter:on
    }

    @Test
    @Transactional
    public void delete() throws IOException, SQLException, Exception {
        File file = createEntity(PATH_FILE, entityManager);
        fileRepository.save(file);

        int databaseSizeBeforeDelete = fileRepository.findAll().size();

        //@formatter:off
        restFileMockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + SLASH + file.getId()))
        .andExpect(status().isOk());
        //@formatter:on

        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
