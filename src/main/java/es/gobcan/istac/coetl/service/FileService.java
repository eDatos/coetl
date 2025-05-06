package es.gobcan.istac.coetl.service;

import org.springframework.web.multipart.MultipartFile;
import es.gobcan.istac.coetl.domain.File;
import java.nio.file.Path;

public interface FileService {
	
    public void uploadRepository(Path etlResourcesPath, MultipartFile file);
    public void updateRepository(Path etlResourcesPath, MultipartFile file, String originalFilename);
    public void deleteRepository(Path etlResourcesPath, String filename);
    public File saveDatabase(MultipartFile fichero);
    public File updateDatabase(MultipartFile file, Long id);
    public void deleteDatabase(Long id);
    public File toFile(MultipartFile fichero);
}
