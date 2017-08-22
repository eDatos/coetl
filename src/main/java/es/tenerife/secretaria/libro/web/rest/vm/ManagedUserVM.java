package es.tenerife.secretaria.libro.web.rest.vm;

import java.time.Instant;
import java.util.Set;

import es.tenerife.secretaria.libro.web.rest.dto.UserDTO;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedUserVM extends UserDTO {

	public ManagedUserVM() {
		// Empty constructor needed for Jackson.
	}

	public ManagedUserVM(Long id, String login, String firstName, String lastName, String email, boolean activated,
			String imageUrl, String langKey, String createdBy, Instant createdDate, String lastModifiedBy,
			Instant lastModifiedDate, Set<String> authorities) {

		super(id, login, firstName, lastName, email, activated, imageUrl, langKey, createdBy, createdDate,
				lastModifiedBy, lastModifiedDate, authorities);

	}

	@Override
	public String toString() {
		return "ManagedUserVM{" + "} " + super.toString();
	}
}
