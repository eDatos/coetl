package es.gobcan.istac.coetl.service;

import org.springframework.web.multipart.MultipartFile;

import es.gobcan.istac.coetl.domain.File;

public interface FileService {

    File save(File documento);

    File findOne(Long id);

    File create(MultipartFile file);

    void removeOrphans();
}
