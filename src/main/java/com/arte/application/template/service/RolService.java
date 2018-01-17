package com.arte.application.template.service;

import java.util.List;
import java.util.Set;

import com.arte.application.template.domain.Rol;

public interface RolService {

    Rol save(Rol operacion);

    List<Rol> findAll(String query);

    Rol findOne(String codigo);

    Rol findOne(Long id);

    void delete(String codigo);

    Set<Rol> findByUsuario(String name);

    List<Rol> findByOperacion(Long operacionId);

}
