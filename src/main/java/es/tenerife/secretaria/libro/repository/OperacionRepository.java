package es.tenerife.secretaria.libro.repository;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.tenerife.secretaria.libro.domain.Operacion;

@SuppressWarnings("unused")
@Repository
public interface OperacionRepository extends JpaRepository<Operacion, Long> {

	Page<Operacion> findAll(DetachedCriteria criteria, Pageable pageable);

	Operacion findBySujetoAndAccion(String sujeto, String accion);

}
