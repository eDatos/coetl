package es.gobcan.istac.coetl.service.impl;

import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.executeCommand;
import static es.gobcan.istac.coetl.platform.common.util.RemoteConnectionUtils.getSudoDestinationOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.Timestamp;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;

import es.gobcan.istac.coetl.config.PentahoProperties;
import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.repository.FileRepository;
import es.gobcan.istac.coetl.service.FileService;
import es.gobcan.istac.coetl.service.validator.FileValidator;


@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
    private PentahoProperties pentahoProperties;
	
	@Autowired
    private FileValidator fileValidator;
	
	@PersistenceContext
	private EntityManager entityManager;
	private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    // Saving file in local repository
    public void uploadRepository(Path etlResourcesPath, MultipartFile file) {
        
        try (OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(pentahoProperties.getHost()));) {
            // Create local temp file
            Path tmpFilePath = Files.createTempFile(file.getOriginalFilename().replace(".", "_").concat("-"), null);
            Files.copy(file.getInputStream(), tmpFilePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Convert all separators to Unix format (/) since we are working with a Linux server
            String remotePath = etlResourcesPath.toString().replace('\\', '/');
            if (!remotePath.endsWith("/")) {
                remotePath += "/";
            }
            String remoteFilePath = remotePath + file.getOriginalFilename();
            
            loadFile(sudoDestinationConnection, remoteFilePath, tmpFilePath);
            
            // Changing permissions on remote server
            changeOwnerFile(sudoDestinationConnection, remoteFilePath);
            
            if (tmpFilePath != null) {
                Files.deleteIfExists(tmpFilePath);
            }
            
        } catch (FileAlreadyExistsException e) {
            throw new RuntimeException("A file with that name already exists.");
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }
    
    private void loadFile(OverthereConnection sudoDestinationConnection, String remoteFilePath, Path tmpFilePath) throws IOException {
        OverthereFile remoteFile = sudoDestinationConnection.getFile(remoteFilePath);

        try (InputStream in = Files.newInputStream(tmpFilePath);
             OutputStream out = remoteFile.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

	@Override
	// Update file in local repository
	public void updateRepository(Path etlResourcesPath, MultipartFile file, String originalFilename) {
		deleteRepository(etlResourcesPath, originalFilename);
		uploadRepository(etlResourcesPath, file);
	}

	@Override
	public void deleteRepository(Path etlResourcesPath, String filename) {
	    String remotePath = etlResourcesPath.toString().replace('\\', '/');
		OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(pentahoProperties.getHost()));
		try {
			executeCommand(sudoDestinationConnection, "rm", remotePath.toString().concat("/").concat(filename));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			sudoDestinationConnection.close();
		}
	}

	private void changeOwnerFile(OverthereConnection sudoConnection, String pathFileToChange) {
		String chownParameter = pentahoProperties.getHost().getOwnerUserResourcesPath().concat(":").concat(pentahoProperties.getHost().getOwnerGroupResourcesPath());
        executeCommand(sudoConnection, "chown", chownParameter, pathFileToChange);
        executeCommand(sudoConnection, "chmod", "644", pathFileToChange);
    }

	@Override
	public File saveDatabase(MultipartFile fichero) {
		File documento = this.toFile(fichero);
        fileValidator.validate(documento);
        return fileRepository.saveAndFlush(documento);
	}
	
	@Override
	public void deleteDatabase(Long id) {
		fileRepository.delete(id);
	}
	
	@Override
	public File updateDatabase(MultipartFile file, Long id) {
		File fichero = this.toFile(file);
		fileValidator.validate(fichero);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		File realFile = fileRepository.getOne(id);
		realFile.setContent(fichero.getContent());
		realFile.setCreationDate(timestamp);
		realFile.setFormat(fichero.getFormat());
		realFile.setName(fichero.getName());

		return null;
	}

	@Override
	public File toFile(MultipartFile fichero) {
		Blob data;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            data = Hibernate.getLobCreator((Session) entityManager.getDelegate()).createBlob(fichero.getInputStream(), fichero.getSize());
        } catch (IOException e) {
            throw new ValidationException(e);
        }

        File documento = new File();
        documento.setName(fichero.getOriginalFilename());
        documento.setContent(data);
        documento.setFormat(fichero.getContentType());
        documento.setCreationDate(timestamp);
        return documento;
	}

}
