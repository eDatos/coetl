package es.tenerife.secretaria.libro.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.tenerife.secretaria.libro.domain.Operacion;

public interface OperacionService {
	Page<Operacion> findAll(Pageable pageable, String query);
	
	List<Operacion> findAll();

	Operacion findOne(Long id);

	Operacion findBySujetoAndAccion(String sujeto, String accion);

}
