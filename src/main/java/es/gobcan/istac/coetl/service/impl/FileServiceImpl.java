package es.gobcan.istac.coetl.service.impl;

import java.io.IOException;
import java.sql.Blob;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.repository.FileRepository;
import es.gobcan.istac.coetl.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private Logger log = LoggerFactory.getLogger(FileService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public File save(File file) {
        log.debug("Request to save a File : {}", file);
        return fileRepository.saveAndFlush(file);
    }

    @Override
    public File findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findOne(id);
    }

    @Override
    public File create(MultipartFile file) {
        log.debug("Request to save File : {}", file.getOriginalFilename());

        Blob data;

        try {
            data = Hibernate.getLobCreator((Session) entityManager.getDelegate()).createBlob(file.getInputStream(), file.getSize());
        } catch (IOException e) {
            throw new ValidationException(e);
        }

        File documento = new File();
        documento.setName(file.getOriginalFilename());
        documento.setData(data);
        documento.setLength(file.getSize());
        documento.setDataContentType(file.getContentType());
        return fileRepository.saveAndFlush(documento);
    }

    @Override
    public void removeOrphans() {
        fileRepository.removeOrphans();
    }
}
