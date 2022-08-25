package es.gobcan.istac.coetl.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public void removeOrphans() {
        fileRepository.removeOrphans();
    }
}
