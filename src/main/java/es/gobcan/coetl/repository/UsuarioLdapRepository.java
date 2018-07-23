package es.gobcan.coetl.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import es.gobcan.coetl.entry.UsuarioLdapEntry;

public interface UsuarioLdapRepository extends LdapRepository<UsuarioLdapEntry> {

}
