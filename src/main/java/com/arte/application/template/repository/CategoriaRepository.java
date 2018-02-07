package com.arte.application.template.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arte.application.template.domain.Categoria;

/**
 * Spring Data JPA repository for the Categoria entity.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Set<Categoria> save(Set<Categoria> categorias);
}
