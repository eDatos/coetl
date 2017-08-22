package es.tenerife.secretaria.libro.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.tenerife.secretaria.libro.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByActivationKey(String activationKey);

	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByLogin(String login);

	@EntityGraph(attributePaths = "authorities")
	User findOneWithAuthoritiesById(Long id);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByLogin(String login);

	Page<User> findAllByLoginNot(Pageable pageable, String login);
}
