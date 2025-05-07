package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    
	File findOneById(Long id);
}
