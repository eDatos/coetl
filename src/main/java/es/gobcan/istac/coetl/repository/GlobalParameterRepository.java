package es.gobcan.istac.coetl.repository;

import es.gobcan.istac.coetl.domain.GlobalParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalParameterRepository extends JpaRepository<GlobalParameter, Long> {

    GlobalParameter findByKeyAndIdNot(String key, Long id);
    Page<GlobalParameter> findAll(Pageable pageable);
    GlobalParameter findByKey(String key);
    GlobalParameter findOneById(Long id);
}
