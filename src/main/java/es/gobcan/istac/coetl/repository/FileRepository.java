package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    //@formatter:off
    @Modifying
    @Query(value = "DELETE FROM tb_files f "
            + "WHERE NOT EXISTS (SELECT 1 FROM tb_etls e WHERE e.etl_file_fk = f.id OR e.etl_description_file_fk = f.id)"
            , nativeQuery = true)
    //@formatter:on
    void removeOrphans();
}
