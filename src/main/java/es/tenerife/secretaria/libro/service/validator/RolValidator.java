package es.tenerife.secretaria.libro.service.validator;

import org.springframework.stereotype.Component;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.web.rest.errors.CustomParameterizedException;

@Component
public class RolValidator extends AbstractValidator<Rol> {

	private static final String ERROR_CODE_ROL_NEED_OPERACIONES = "error.rol.validation.rol-necesita-operaciones";

	@Override
	public void validate(Rol rol) {
		if (rol != null) {
			checkRolHasOperaciones(rol);
		}
	}

	private void checkRolHasOperaciones(Rol rol) {
		if (rol.getOperaciones() == null || rol.getOperaciones().isEmpty()) {
			throw new CustomParameterizedException(ERROR_CODE_ROL_NEED_OPERACIONES);
		}
	}

}
