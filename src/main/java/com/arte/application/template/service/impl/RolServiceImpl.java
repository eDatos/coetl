
package com.arte.application.template.service.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.arte.application.template.domain.Rol;
import com.arte.application.template.repository.RolRepository;
import com.arte.application.template.repository.UsuarioRepository;
import com.arte.application.template.service.RolService;
import com.arte.application.template.service.validator.RolValidator;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.application.template.web.rest.errors.ErrorConstants;
import com.arte.application.template.web.rest.util.QueryUtil;

@Service
public class RolServiceImpl implements RolService {

    private RolRepository rolRepository;

    private UsuarioRepository usuarioRepository;

    private final Logger log = LoggerFactory.getLogger(RolServiceImpl.class);

    private QueryUtil queryUtil;

    private RolValidator rolValidator;

    public RolServiceImpl(RolRepository rolRepository, UsuarioRepository usuarioRepository, QueryUtil queryUtil, RolValidator rolValidator) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.queryUtil = queryUtil;
        this.rolValidator = rolValidator;
    }

    @Override
    public Rol save(Rol rol) {
        log.debug("Petición para guardar rol {}", rol);
        rolValidator.validate(rol);
        return rolRepository.save(rol);
    }

    @Override
    public List<Rol> findAll(String query) {
        log.debug("Petición para buscar roles con query {}", query);
        DetachedCriteria criteria = queryUtil.queryToRolCriteria(null, query);
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
            throw new CustomParameterizedException("error.rol.users-has-role", codigo);
        }
        Rol rol = rolRepository.findOneByCodigo(codigo);
        if (rol == null) {
            throw new CustomParameterizedException(String.format("Role '%s' not found", codigo), ErrorConstants.ROL_NO_ENCONTRADO, codigo);
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
