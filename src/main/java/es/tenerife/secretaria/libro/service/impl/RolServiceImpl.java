
package es.tenerife.secretaria.libro.service.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.tenerife.secretaria.libro.domain.Rol;
import es.tenerife.secretaria.libro.repository.RolRepository;
import es.tenerife.secretaria.libro.repository.UsuarioRepository;
import es.tenerife.secretaria.libro.service.RolService;
import es.tenerife.secretaria.libro.web.rest.errors.CustomParameterizedException;
import es.tenerife.secretaria.libro.web.rest.errors.ErrorConstants;
import es.tenerife.secretaria.libro.web.rest.util.QueryUtil;

@Service
public class RolServiceImpl implements RolService {

	private RolRepository rolRepository;

	private UsuarioRepository usuarioRepository;

	private final Logger log = LoggerFactory.getLogger(RolServiceImpl.class);

	private QueryUtil queryUtil;

	public RolServiceImpl(RolRepository rolRepository, UsuarioRepository usuarioRepository, QueryUtil queryUtil) {
		this.rolRepository = rolRepository;
		this.usuarioRepository = usuarioRepository;
		this.queryUtil = queryUtil;
	}

	@Override
	public Rol save(Rol rol) {
		log.debug("Petición para guardar rol {}", rol);
		return rolRepository.save(rol);
	}

	@Override
	public List<Rol> findAll(String query) {
		log.debug("Petición para buscar roles con query {}", query);
		DetachedCriteria criteria = queryUtil.queryToRolCriteria(query);
		return rolRepository.findAll(criteria);
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
		if (rol == null) {
			throw new CustomParameterizedException(ErrorConstants.ENTIDAD_NO_ENCONTRADA, codigo);
		}
		rolRepository.delete(rol.getId());
	}

	@Override
	public Set<Rol> findByUsuario(String login) {
		log.debug("Petición para buscar roles de usuario {}", login);
		return rolRepository.findByUsuarioLogin(login);
	}

	@Override
	public List<Rol> findByOperacion(Long operacionId) {
		log.debug("Petición para buscar roles de usuario {}", operacionId);
		return rolRepository.findByOperacionesId(operacionId);
	}

}
