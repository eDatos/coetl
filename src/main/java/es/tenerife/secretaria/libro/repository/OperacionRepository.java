package es.tenerife.secretaria.libro.repository;

import es.tenerife.secretaria.libro.domain.Operacion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Operacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperacionRepository extends JpaRepository<Operacion,Long> {
    
}
