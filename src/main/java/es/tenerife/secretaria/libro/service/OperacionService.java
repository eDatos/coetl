package es.tenerife.secretaria.libro.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.tenerife.secretaria.libro.domain.Operacion;

/**
 * Service Interface for managing Operacion.
 */
public interface OperacionService {
	/**
	 * Get all the operacions.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	Page<Operacion> findAll(Pageable pageable, String query);

	/**
	 * Get the "id" operacion.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	Operacion findOne(Long id);

	Operacion findBySujetoAndAccion(String sujeto, String accion);

}
