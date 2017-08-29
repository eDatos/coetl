package es.tenerife.secretaria.libro.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.tenerife.secretaria.libro.domain.Rol;

public interface RolService {

	Rol save(Rol operacion);

	Page<Rol> findAll(Pageable pageable);

	Rol findOne(String name);

	void delete(String name);

}
