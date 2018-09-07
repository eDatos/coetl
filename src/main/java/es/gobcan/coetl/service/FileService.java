package es.gobcan.coetl.service;

import org.springframework.web.multipart.MultipartFile;

import es.gobcan.coetl.domain.File;

public interface FileService {

    File save(File documento);

    File findOne(Long id);

    File create(MultipartFile file);

    void removeOrphans();
}
