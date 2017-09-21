package es.tenerife.secretaria.libro.service;

import java.util.Set;

import es.tenerife.secretaria.libro.domain.Rol;

public interface RolService {

	Rol save(Rol operacion);

	Set<Rol> findAll(String query);

	Rol findOne(String codigo);

	Rol findOne(Long id);

	void delete(String codigo);

	Set<Rol> findByUsuario(String name);

	Set<Rol> findByOperacion(Long operacionId);

}
