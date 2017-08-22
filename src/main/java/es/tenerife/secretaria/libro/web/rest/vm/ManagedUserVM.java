package es.tenerife.secretaria.libro.web.rest.vm;

import es.tenerife.secretaria.libro.web.rest.dto.UserDTO;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedUserVM extends UserDTO {

	public ManagedUserVM() {
		// Empty constructor needed for Jackson.
	}

	public ManagedUserVM(ManagedUserVM userDTO) {
		super();
		updateFrom(userDTO);
	}

	@Override
	public String toString() {
		return "ManagedUserVM{" + "} " + super.toString();
	}
}
