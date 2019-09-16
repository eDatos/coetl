package es.gobcan.istac.coetl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.Parameter.Type;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> findAllByEtlId(Long etlId);
    Parameter findByIdAndEtlId(Long id, Long etlId);
    Parameter findByKeyAndEtlId(String key, Long etlId);
    Parameter findByKeyAndEtlIdAndIdNot(String key, Long etlId, Long id);
    List<Parameter> findAllByEtlIdAndType(Long eltId, Type auto);
}
