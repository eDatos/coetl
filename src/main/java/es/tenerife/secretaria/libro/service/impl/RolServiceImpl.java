package es.tenerife.secretaria.libro.service.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.service.RolService;

@Service
public class RolServiceImpl implements RolService {

	private RolRepository rolRepository;

	private final Logger log = LoggerFactory.getLogger(RolServiceImpl.class);

	public RolServiceImpl(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
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
	public Rol findOne(String name) {
		log.debug("Petición para buscar rol {}", name);
		return rolRepository.findOneByNombre(name);
	}

	@Override
	public Rol findOne(Long id) {
		log.debug("Petición para buscar rol {}", id);
		return rolRepository.findOne(id);
	}

	@Override
	public void delete(String name) {
		log.debug("Petición para eliminar rol {}", name);
		Rol rol = rolRepository.findOneByNombre(name);
		rolRepository.delete(rol.getId());
	}

	@Override
	public Set<Rol> findByUsuario(String login) {
		log.debug("Petición para buscar roles de usuario {}", login);
		return rolRepository.findByUsuarioLogin(login);
	}

}
