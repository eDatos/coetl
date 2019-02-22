package es.gobcan.istac.coetl.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import es.gobcan.istac.coetl.entry.UsuarioLdapEntry;

public interface UsuarioLdapRepository extends LdapRepository<UsuarioLdapEntry> {

}
