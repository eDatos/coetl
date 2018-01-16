package com.arte.application.template.service;

import java.util.List;

import com.arte.application.template.domain.Operacion;

public interface OperacionService {

	List<Operacion> findAll(String query);

	Operacion findOne(Long id);

	Operacion findBySujetoAndAccion(String sujeto, String accion);

}
