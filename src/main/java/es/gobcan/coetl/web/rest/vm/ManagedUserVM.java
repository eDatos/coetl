package es.gobcan.coetl.web.rest.vm;

import es.gobcan.coetl.web.rest.dto.UsuarioDTO;

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
        return "ManagedUserVM (id = " + getId() + ", Nombre = " + getNombre() + ", Apellido1 = " + getApellido1() + ", Apellido2 = " + getApellido2() + ")";
    }
}
