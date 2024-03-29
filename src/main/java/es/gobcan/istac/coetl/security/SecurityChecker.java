package es.gobcan.istac.coetl.security;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.enumeration.Rol;

@Component("secChecker")
public class SecurityChecker {
    
    private static final String ACL_APP_NAME = "GESTOR_CONSOLA_ETL";
    private static final String SEPARATOR = "#";

    private boolean hasRole(Authentication authentication, Rol... userRoles) {
        return authentication.getAuthorities().stream().anyMatch(authority -> {
            String[] appRole = authority.getAuthority().split(SEPARATOR);
            String application = appRole[0];
            String roleName = appRole[1];
            return application.equals(ACL_APP_NAME) && Arrays.stream(userRoles).anyMatch(role -> Objects.equals(role.name(), roleName));
        });
    }
    
    public boolean puedeConsultarAuditoria(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarLogs(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeModificarLogs(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarUsuario(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarUsuarioLdap(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeCrearUsuario(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeModificarUsuario(Authentication authentication, String login) {
        String userLogin = SecurityUtils.getCurrentUserLogin();
        return this.isAdmin(authentication) || (StringUtils.isNotBlank(userLogin) && StringUtils.isNotBlank(login) && userLogin.equals(login));
    }

    public boolean puedeBorrarUsuario(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean canReadFile(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication) || this.isLector(authentication);
    }

    public boolean canManageFile(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication);
    }

    public boolean puedeConsultarMetrica(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarSalud(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeGestionarSalud(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarConfig(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean puedeConsultarApi(Authentication authentication) {
        return this.isAdmin(authentication);
    }

    public boolean canReadEtl(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication) || this.isLector(authentication);
    }

    public boolean canManageEtl(Authentication authentication) {
        return this.isAdmin(authentication) || this.isTecnico(authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        return this.hasRole(authentication, Rol.ADMINISTRADOR);
    }

    private boolean isTecnico(Authentication authentication) {
        return this.hasRole(authentication, Rol.TECNICO_PRODUCCION);
    }

    private boolean isLector(Authentication authentication) {
        return this.hasRole(authentication, Rol.LECTOR);
    }
}