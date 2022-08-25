package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.File;

public interface FileService {

    File save(File documento);

    File findOne(Long id);

    void removeOrphans();
}
