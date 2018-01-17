package com.arte.application.template.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.QueryHint;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.arte.application.template.domain.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    @QueryHints(value = {@QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "MANUAL")})
    Rol findOneByCodigo(String nombre);

    @Query("select r from Usuario u inner join u.roles r where u.login = ?1")
    Set<Rol> findByUsuarioLogin(String login);

    List<Rol> findByOperacionesId(Long operacionId);

    List<Rol> findAll(DetachedCriteria criteria);
}
