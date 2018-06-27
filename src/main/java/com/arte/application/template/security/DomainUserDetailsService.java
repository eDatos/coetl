package com.arte.application.template.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.arte.application.template.domain.Operacion;
import com.arte.application.template.domain.Rol;
import com.arte.application.template.domain.Usuario;
import com.arte.application.template.repository.UsuarioRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UsuarioRepository userRepository;

    public DomainUserDetailsService(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<Usuario> userFromDatabase = userRepository.findOneWithRolesByLoginAndDeletionDateIsNull(lowercaseLogin);
        if (!userFromDatabase.isPresent()) {
            return new User(lowercaseLogin, "", new ArrayList<>());
        }

        return userFromDatabase.map(user -> {
            if (user.getDeletionDate() != null) {
                throw new UserNotActivatedException("Usuario " + lowercaseLogin + " no estaba activado");
            }
            List<GrantedAuthority> permisos = new ArrayList<>();
            if (user.getRoles() != null) {
                for (Rol rolAux : user.getRoles()) {
                    sumarPermisos(permisos, rolAux);
                }
            }
            return new User(lowercaseLogin, "", permisos);
        }).orElseThrow(() -> new UsernameNotFoundException("Usuario " + lowercaseLogin + " no encontrado en la " + "database"));
    }

    private void sumarPermisos(List<GrantedAuthority> permisos, Rol rolAux) {
        if (rolAux.getOperaciones() != null) {
            for (Operacion operacionAux : rolAux.getOperaciones()) {
                permisos.add(new SimpleGrantedAuthority(operacionAux.getAccion() + "_" + operacionAux.getSujeto()));
            }
        }
    }
}
