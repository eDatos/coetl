package com.arte.application.template.service.validator;

import org.springframework.stereotype.Component;

import com.arte.application.template.domain.Rol;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.application.template.web.rest.errors.ErrorConstants;

@Component
public class RolValidator extends AbstractValidator<Rol> {

    @Override
    public void validate(Rol rol) {
        if (rol != null) {
            checkRolHasOperaciones(rol);
        }
    }

    private void checkRolHasOperaciones(Rol rol) {
        if (rol.getOperaciones() == null || rol.getOperaciones().isEmpty()) {
            throw new CustomParameterizedException("Role need Operations", ErrorConstants.ROL_NECESITA_OPERACIONES);
        }
    }

}
