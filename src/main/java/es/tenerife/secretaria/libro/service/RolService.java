package es.tenerife.secretaria.libro.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.tenerife.secretaria.libro.domain.Rol;

public interface RolService {

	Rol save(Rol operacion);

	Page<Rol> findAll(Pageable pageable);

	Rol findOne(String codigo);

	Rol findOne(Long id);

	void delete(String codigo);

	Set<Rol> findByUsuario(String name);

	Page<Rol> findByOperacion(Long operacionId, Pageable pageable);

}
