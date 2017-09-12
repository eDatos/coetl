package es.tenerife.secretaria.libro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.tenerife.secretaria.libro.domain.Rol;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {

	Rol findOneByNombre(String nombre);
}
