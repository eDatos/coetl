package es.gobcan.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
