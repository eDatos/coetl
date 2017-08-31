package es.tenerife.secretaria.libro.service;

import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;

@FunctionalInterface
public interface LdapService {

	public UsuarioLdapEntry buscarUsuarioLdap(String nombreUsuario);

}
