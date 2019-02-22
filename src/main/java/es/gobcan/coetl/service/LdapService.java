package es.gobcan.coetl.service;

import es.gobcan.coetl.entry.UsuarioLdapEntry;

@FunctionalInterface
public interface LdapService {

    public UsuarioLdapEntry buscarUsuarioLdap(String nombreUsuario);

}
