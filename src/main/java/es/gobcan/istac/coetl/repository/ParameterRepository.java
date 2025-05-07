package es.gobcan.istac.coetl.repository;

import es.gobcan.istac.coetl.domain.Parameter;
import es.gobcan.istac.coetl.domain.Parameter.Type;
import es.gobcan.istac.coetl.domain.Parameter.Typology;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> findAllByEtlId(Long etlId);

    List<Parameter> findAllByEtlIdOrEtlNullAndType(Long etlId, Type type);

    Parameter findByIdAndEtlId(Long id, Long etlId);

    Parameter findByKeyAndEtlId(String key, Long etlId);
    
    Parameter findOneByKeyAndEtlId(String key, Long etlId);

    Parameter findByKeyAndType(String key, Type type);

    Parameter findByKey(String key);

    Parameter findOneById(Long id);

    Parameter findByKeyAndEtlIdAndIdNot(String key, Long etlId, Long id);

    Parameter findByKeyAndIdNot(String key, Long id);
    
    List<Parameter> findAllByEtlIdAndType(Long eltId, Type auto);
    
    List<Parameter> findAllByEtlIdAndTypology(Long eltId, Typology typology);

    Page<Parameter> findAllByType(Type type, Pageable pageable);
    
}
