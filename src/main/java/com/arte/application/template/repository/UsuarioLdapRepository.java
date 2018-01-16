package com.arte.application.template.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import com.arte.application.template.entry.UsuarioLdapEntry;

public interface UsuarioLdapRepository extends LdapRepository<UsuarioLdapEntry> {

}
