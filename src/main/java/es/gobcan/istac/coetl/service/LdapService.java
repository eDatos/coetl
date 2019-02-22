package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.entry.UsuarioLdapEntry;

@FunctionalInterface
public interface LdapService {

    public UsuarioLdapEntry buscarUsuarioLdap(String nombreUsuario);

}
