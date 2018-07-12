package com.arte.application.template.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import com.arte.application.template.config.ApplicationProperties;
import com.arte.application.template.entry.UsuarioLdapEntry;
import com.arte.application.template.repository.UsuarioLdapRepository;
import com.arte.application.template.service.LdapService;

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
