package es.tenerife.secretaria.libro.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.service.RolService;
import es.tenerife.secretaria.libro.web.rest.errors.CustomParameterizedException;

@Service
public class RolServiceImpl implements RolService {

	private RolRepository rolRepository;
	
	private UsuarioRepository usuarioRepository;

	private final Logger log = LoggerFactory.getLogger(RolServiceImpl.class);

	public RolServiceImpl(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
		this.rolRepository = rolRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Rol save(Rol rol) {
		log.debug("Request to save rol {}", rol);
		return rolRepository.save(rol);
	}

	@Override
	public Page<Rol> findAll(Pageable pageable) {
		log.debug("Request to find roles");
		return rolRepository.findAll(pageable);
	}

	@Override
	public Rol findOne(String codigo) {
		log.debug("Petición para buscar rol {}", codigo);
		return rolRepository.findOneByCodigo(codigo);
	}

	@Override
	public Rol findOne(Long id) {
		log.debug("Petición para buscar rol {}", id);
		return rolRepository.findOne(id);
	}

	@Override
	public void delete(String codigo) {
		log.debug("Petición para eliminar rol {}", codigo);
		if (!usuarioRepository.findAllByRolesCodigo(codigo).isEmpty()) {
			throw new CustomParameterizedException("error.palabraClave.users-has-role", codigo);
		}
		Rol rol = rolRepository.findOneByCodigo(codigo);
		rolRepository.delete(rol.getId());
	}

	@Override
	public Set<Rol> findByUsuario(String login) {
		log.debug("Petición para buscar roles de usuario {}", login);
		return rolRepository.findByUsuarioLogin(login);
	}

}
