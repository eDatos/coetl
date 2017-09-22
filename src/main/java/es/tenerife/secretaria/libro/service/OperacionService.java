package es.tenerife.secretaria.libro.service;

import java.util.List;

import es.tenerife.secretaria.libro.domain.Operacion;

public interface OperacionService {

	List<Operacion> findAll(String query);

	Operacion findOne(Long id);

	Operacion findBySujetoAndAccion(String sujeto, String accion);

}
