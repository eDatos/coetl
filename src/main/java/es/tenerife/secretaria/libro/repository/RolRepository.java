package es.tenerife.secretaria.libro.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.tenerife.secretaria.libro.domain.Rol;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {

	Rol findOneByNombre(String nombre);

	@Query("select r from Rol r, Usuario u where u.login = ?1")
	Set<Rol> findByUsuarioLogin(String login);
}
