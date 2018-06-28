package com.arte.application.template.repository;

import java.util.Optional;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arte.application.template.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findOneByEmail(String email);

    Optional<Usuario> findOneByLogin(String login);

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findOneWithRolesByLogin(String login);

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findOneWithRolesByLoginAndDeletionDateIsNull(String login);

    @EntityGraph(attributePaths = "roles")
    Usuario findOneWithRolesByIdAndDeletionDateIsNull(Long id);

    Optional<Usuario> findOneByLoginAndDeletionDateIsNull(String login);

    Page<Usuario> findAll(DetachedCriteria criteria, Pageable pageable);
}