package es.tenerife.secretaria.libro.repository;

import es.tenerife.secretaria.libro.domain.Rol;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface RolRepository extends JpaRepository<Rol, String> {
}
