package es.tenerife.secretaria.libro.repository;

import org.springframework.data.ldap.repository.LdapRepository;

import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;

public interface UsuarioLdapRepository extends LdapRepository<UsuarioLdapEntry> {

}
