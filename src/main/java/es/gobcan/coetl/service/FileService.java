package es.gobcan.coetl.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.gobcan.coetl.domain.File;

public interface FileService {

    File save(File documento);

    List<File> findAll();

    File findOne(Long id);

    void delete(Long id);

    File create(MultipartFile file);
}
