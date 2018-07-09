package com.arte.application.template.web.rest.vm;

import com.arte.application.template.web.rest.dto.UsuarioDTO;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedUserVM extends UsuarioDTO {

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserVM(ManagedUserVM userDTO) {
        super();
        updateFrom(userDTO);
    }

    @Override
    public String toString() {
        return "ManagedUserVM (id = " + getId() +  ")";
    }
}
