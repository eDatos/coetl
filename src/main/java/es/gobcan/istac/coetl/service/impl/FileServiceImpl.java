package es.gobcan.istac.coetl.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.repository.FileRepository;
import es.gobcan.istac.coetl.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @PersistenceContext
    private EntityManager entityManager;

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void removeOrphans() {
        fileRepository.removeOrphans();
    }
}
