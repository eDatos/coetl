package es.gobcan.istac.coetl.service;

import org.springframework.web.multipart.MultipartFile;
import es.gobcan.istac.coetl.domain.File;

public interface FileService {
	
    public void uploadRepository(String etlResourcesPath, MultipartFile file);
    public void updateRepository(String etlResourcesPath, MultipartFile file, String originalFilename);
    public void deleteRepository(String etlResourcesPath, String filename);
    public File saveDatabase(MultipartFile fichero);
    public File updateDatabase(MultipartFile file, Long id);
    public void deleteDatabase(Long id);
    public File toFile(MultipartFile fichero);
}
