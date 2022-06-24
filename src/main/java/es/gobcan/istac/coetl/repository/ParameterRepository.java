package es.gobcan.istac.coetl.repository;

import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.Parameter.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> findAllByEtlId(Long etlId);
    Parameter findByIdAndEtlId(Long id, Long etlId);
    Parameter findByKeyAndEtlId(String key, Long etlId);
    Parameter findByKeyAndEtlIdAndIdNot(String key, Long etlId, Long id);
    List<Parameter> findAllByEtlIdAndType(Long eltId, Type auto);

    Page<Parameter> findAllByType(Type type, Pageable pageable);
    Parameter findByKey(String key);
    Parameter findOneById(Long id);
}
