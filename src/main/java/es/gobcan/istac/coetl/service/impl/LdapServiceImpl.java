package es.gobcan.istac.coetl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.config.ApplicationProperties;
import es.gobcan.istac.coetl.entry.UsuarioLdapEntry;
import es.gobcan.istac.coetl.repository.UsuarioLdapRepository;
import es.gobcan.istac.coetl.service.LdapService;

@Service
public class LdapServiceImpl implements LdapService {

    private String searchProperty;

    public LdapServiceImpl(ApplicationProperties applicationProperties) {
        this.searchProperty = applicationProperties.getLdap().getSearchUsersProperty();
    }

    @Autowired
    private UsuarioLdapRepository usuarioLdapRepository;

    @Override
    public UsuarioLdapEntry buscarUsuarioLdap(String nombreUsuario) {
        LdapQuery query = LdapQueryBuilder.query().where(searchProperty).is(nombreUsuario);
        return usuarioLdapRepository.findOne(query);
    }

}
