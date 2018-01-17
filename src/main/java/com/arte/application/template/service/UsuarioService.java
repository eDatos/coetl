package com.arte.application.template.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.config.Constants;
import com.arte.application.template.domain.Operacion;
import com.arte.application.template.domain.Rol;
import com.arte.application.template.domain.Usuario;
import com.arte.application.template.repository.RolRepository;
import com.arte.application.template.repository.UsuarioRepository;
import com.arte.application.template.security.SecurityUtils;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.application.template.web.rest.util.QueryUtil;

@Service
public class UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    private final RolRepository authorityRepository;

    private LdapService ldapService;

    private QueryUtil queryUtil;

    public UsuarioService(UsuarioRepository userRepository, RolRepository authorityRepository, LdapService ldapService, QueryUtil queryUtil) {
        this.usuarioRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.ldapService = ldapService;
        this.queryUtil = queryUtil;
    }

    public Usuario createUsuario(@NotNull Usuario user) {
        validarUsuarioLdap(user);
        Usuario newUser = new Usuario();
        newUser.setLogin(user.getLogin());
        newUser.setNombre(user.getNombre());
        newUser.setApellido1(user.getApellido1());
        newUser.setApellido2(user.getApellido2());
        newUser.setEmail(user.getEmail());
        if (user.getRoles() != null) {
            Set<Rol> authorities = new HashSet<>();
            user.getRoles().forEach(authority -> authorities.add(authorityRepository.findOneByCodigo(authority.getCodigo())));
            newUser.setRoles(authorities);
        }
        usuarioRepository.save(newUser);
        log.debug("Creada informaicón para el usuario: {}", newUser);
        return newUser;
    }

    public void updateUsuario(String firstName, String apellido1, String apellido2, String email) {
        usuarioRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            validarUsuarioLdap(user);
            user.setNombre(firstName);
            user.setApellido1(apellido1);
            user.setApellido2(apellido2);
            user.setEmail(email);
            log.debug("Cambiado información para el usuario: {}", user);
        });
    }

    public Usuario updateUsuario(Usuario user) {
        validarUsuarioLdap(user);
        return usuarioRepository.save(user);
    }

    public void deleteUsuario(String login) {
        usuarioRepository.findOneByLoginAndDeletionDateIsNull(login).ifPresent(user -> {
            user.setDeletionDate(ZonedDateTime.now());
            usuarioRepository.save(user);
            log.debug("Eliminado Usuario: {}", user);
        });
    }

    public void restore(Usuario usuario) {
        if (usuario == null) {
            throw new CustomParameterizedException("error.userManagement.usuario-no-valido");
        }
        usuario.setDeletionDate(null);
        usuarioRepository.save(usuario);
        log.debug("Restaurado usuario: {}", usuario);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> getAllUsuarios(Pageable pageable, Boolean includeDeleted, String query) {
        DetachedCriteria criteria = buildUsuarioCriteria(pageable, includeDeleted, query);
        return usuarioRepository.findAll(criteria, pageable);
    }

    private DetachedCriteria buildUsuarioCriteria(Pageable pageable, Boolean includeDeleted, String query) {
        StringBuilder queryBuilder = new StringBuilder();
        initializeQueryBuilder(query, queryBuilder);
        transalateSort(pageable, queryBuilder);
        String finalQuery = getFinalQuery(includeDeleted, queryBuilder);
        return queryUtil.queryToUserCriteria(finalQuery);
    }

    private void initializeQueryBuilder(String query, StringBuilder queryBuilder) {
        if (StringUtils.isNotBlank(query)) {
            queryBuilder.append("(" + query + ") AND ");
        }
        queryBuilder.append(" LOGIN NE '").append(Constants.ANONYMOUS_USER).append("'");
    }

    private String getFinalQuery(Boolean includeDeleted, StringBuilder queryBuilder) {
        String finalQuery = queryBuilder.toString();
        if (BooleanUtils.isTrue(includeDeleted)) {
            finalQuery = queryUtil.queryIncludingDeleted(finalQuery);
        }
        return finalQuery;
    }

    private void transalateSort(Pageable pageable, StringBuilder queryBuilder) {
        if (pageable != null) {
            queryBuilder.append(queryUtil.pageableSortToQueryString(pageable));
        }
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioWithAuthoritiesByLogin(String login, Boolean includeDeleted) {
        if (BooleanUtils.isTrue(includeDeleted)) {
            return usuarioRepository.findOneByLogin(login);
        } else {
            return usuarioRepository.findOneWithRolesByLoginAndDeletionDateIsNull(login);
        }
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioWithAuthorities(Long id) {
        return usuarioRepository.findOneWithRolesByIdAndDeletionDateIsNull(id);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioWithAuthorities() {
        Usuario returnValue = usuarioRepository.findOneWithRolesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(new Usuario());
        if (returnValue.getDeletionDate() != null) {
            returnValue.setRoles(generarRolesVacios());
        }

        return returnValue;
    }

    private void validarUsuarioLdap(Usuario user) {
        if (ldapService.buscarUsuarioLdap(user.getLogin()) == null) {
            throw new CustomParameterizedException("error.userManagement.usuario-ldap-no-encontrado", user.getLogin());
        }
    }

    private Set<Rol> generarRolesVacios() {
        Set<Rol> returnValue = new HashSet<>();
        Set<Operacion> operacionesList = new HashSet<>();

        Rol loginRol = new Rol();
        Operacion loginOperacion = new Operacion();

        // FIXME: Eliminar referencias a la plantilla (com.arte.application.template, arte-application-template, etc...)
        loginOperacion.setAccion("LOGIN");
        loginOperacion.setSujeto("ARTE_APPLICATION_TEMPLATE");

        loginRol.setOperaciones(operacionesList);
        return returnValue;
    }
}