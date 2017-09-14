package es.tenerife.secretaria.libro.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.tenerife.secretaria.libro.domain.Usuario;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	List<Usuario> findAllByActivadoIsFalseAndCreatedDateBefore(Instant dateTime);

	Optional<Usuario> findOneByEmail(String email);

	Optional<Usuario> findOneByLogin(String login);

	@EntityGraph(attributePaths = "roles")
	Usuario findOneWithRolesById(Long id);

	@EntityGraph(attributePaths = "roles")
	Optional<Usuario> findOneWithRolesByLogin(String login);

	Page<Usuario> findAllByLoginNot(Pageable pageable, String login);

	Page<Usuario> findAllByLoginNotAndDeletionDateIsNull(Pageable pageable, String anonymousUser);

	@EntityGraph(attributePaths = "roles")
	Optional<Usuario> findOneWithRolesByLoginAndDeletionDateIsNull(String login);

	@EntityGraph(attributePaths = "roles")
	Usuario findOneWithRolesByIdAndDeletionDateIsNull(Long id);

	List<Usuario> findAllByActivadoIsFalseAndCreatedDateBeforeAndDeletionDateIsNull(Instant minus);

	Optional<Usuario> findOneByLoginAndDeletionDateIsNull(String login);

	Usuario findOneByIdAndDeletionDateIsNull(Long id);

	Page<Usuario> findAll(DetachedCriteria criteria, Pageable pageable);
	
	List<Usuario> findAllByRoles_Nombre(String nombre);
}
