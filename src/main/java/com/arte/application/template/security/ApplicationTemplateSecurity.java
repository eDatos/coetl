package com.arte.application.template.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.arte.application.template.domain.enumeration.Rol;

@Component("secChecker")
public class ApplicationTemplateSecurity {

    public boolean puedeConsultarAuditoria(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarLogs(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeModificarLogs(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarUsuario(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarUsuarioLdap(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeCrearUsuario(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeModificarUsuario(Authentication authentication, String login) {
        String userLogin = SecurityUtils.getCurrentUserLogin();
        return this.esAdmin(authentication) || (StringUtils.isNotBlank(userLogin) && StringUtils.isNotBlank(login) && userLogin.equals(login));
    }

    public boolean puedeBorrarUsuario(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarCategoria(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeCrearCategoria(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeModificarCategoria(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeBorrarCategoria(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeConsultarIdioma(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeCrearIdioma(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeModificarIdioma(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeBorrarIdioma(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeConsultarDocumento(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeCrearDocumento(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeModificarDocumento(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeBorrarDocumento(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeConsultarActor(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeCrearActor(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeModificarActor(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeBorrarActor(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeConsultarPelicula(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeCrearPelicula(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeModificarPelicula(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeBorrarPelicula(Authentication authentication) {
        return this.esUsuario(authentication);
    }

    public boolean puedeConsultarMetrica(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarSalud(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarConfig(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    public boolean puedeConsultarApi(Authentication authentication) {
        return this.esAdmin(authentication);
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(Rol.ADMIN.name()));
    }

    private boolean esUsuario(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(Rol.USER.name()));
    }
}