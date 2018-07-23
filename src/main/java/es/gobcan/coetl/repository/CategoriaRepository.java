package es.gobcan.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.coetl.domain.Categoria;

/**
 * Spring Data JPA repository for the Categoria entity.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
