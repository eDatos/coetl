package es.tenerife.secretaria.libro.repository;

import java.util.Set;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import es.tenerife.secretaria.libro.domain.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

	@QueryHints(value = { @QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "MANUAL") })
	Rol findOneByCodigo(String nombre);

	@Query("select r from Rol r, Usuario u where u.login = ?1")
	Set<Rol> findByUsuarioLogin(String login);

	Set<Rol> findByOperacionesId(Long operacionId);
}
