package es.gobcan.istac.coetl.web.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

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
import org.springframework.test.context.junit4.SpringRunner;

import es.gobcan.istac.coetl.CoetlApp;
import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.repository.FileRepository;
import es.gobcan.istac.coetl.service.FileService;
import es.gobcan.istac.coetl.web.rest.mapper.FileMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class FileResourceIntTest {

    private static final String PATH_FILE = "src/main/resources/banner.txt";

    @Autowired
    EntityManager entityManager;

    @Autowired
    FileService fileService;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileRepository fileRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    public static File createEntity(String pathFile, EntityManager entityManager) throws IOException, SQLException {
        File file = buildFileFromFilePath(pathFile, entityManager);
        return file;
    }

    private static File buildFileFromFilePath(String pathFile, EntityManager entityManager) throws IOException, SQLException {
        Path path = Paths.get(pathFile);
        File file = new File();
        file.setName(path.getFileName().toString());
        String dataContentType = Files.probeContentType(path) != null ? Files.probeContentType(path) : "octet/stream";
        file.setDataContentType(dataContentType);
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

}
