package es.tenerife.secretaria.libro.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.tenerife.secretaria.libro.domain.Operacion;

@Repository
public interface OperacionRepository extends JpaRepository<Operacion, Long> {

	List<Operacion> findAll(DetachedCriteria criteria);

	Operacion findBySujetoAndAccion(String sujeto, String accion);

}
