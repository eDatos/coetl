package es.tenerife.secretaria.libro.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import es.tenerife.secretaria.libro.domain.Operacion;
import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.domain.enumeration.TipoAccionOperacion;
import es.tenerife.secretaria.libro.service.OperacionService;
import es.tenerife.secretaria.libro.service.RolService;

@Component
public class OperacionPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	OperacionService operacionService;

	@Autowired
	RolService rolService;

	@Override
	public boolean hasPermission(Authentication auth, Object sujeto, Object accion) {
		if ((auth == null) || (sujeto == null) || !(accion instanceof String)
				|| (TipoAccionOperacion.valueOf((String) accion) == null)) {
			return false;
		}
		String targetType = sujeto instanceof String ? (String) sujeto
				: sujeto.getClass().getSimpleName().toUpperCase();

		return hasPrivilege(auth, targetType, accion.toString().toUpperCase());
	}

	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId, String claseSujeto, Object accion) {
		if ((auth == null) || (claseSujeto == null) || !(accion instanceof String)) {
			return false;
		}
		return hasPrivilege(auth, claseSujeto.toUpperCase(), accion.toString().toUpperCase());
	}

	private boolean hasPrivilege(Authentication auth, String sujeto, String accion) {
		Operacion operacion = operacionService.findBySujetoAndAccion(sujeto, accion);
		if (operacion == null) {
			return false;
		}
		return usuarioHasOperacionRol(auth, operacion);
	}

	private boolean usuarioHasOperacionRol(Authentication auth, Operacion operacion) {
		if (auth == null || operacion == null) {
			return false;
		}
		Set<String> rolesOperacion = operacion.getRoles().stream().map(Rol::getCodigo).map(String::toUpperCase)
				.collect(Collectors.toSet());
		Set<String> rolesUsuario = rolService.findByUsuario(auth.getName()).stream().map(Rol::getCodigo)
				.map(String::toUpperCase).collect(Collectors.toSet());
		return !Collections.disjoint(rolesOperacion, rolesUsuario);
	}

}