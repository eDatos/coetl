package es.tenerife.secretaria.libro.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.tenerife.secretaria.libro.domain.Rol;

public interface RolService {

	Rol save(Rol operacion);

	Page<Rol> findAll(Pageable pageable);

	Rol findOne(String name);

	Rol findOne(Long id);

	void delete(String name);

	Set<Rol> findByUsuario(String name);

}
