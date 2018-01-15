package com.arte.application.template.service;

import com.arte.application.template.entry.UsuarioLdapEntry;

@FunctionalInterface
public interface LdapService {

	public UsuarioLdapEntry buscarUsuarioLdap(String nombreUsuario);

}
