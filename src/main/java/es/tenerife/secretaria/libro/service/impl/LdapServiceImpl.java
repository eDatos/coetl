package es.tenerife.secretaria.libro.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import es.tenerife.secretaria.libro.entry.UsuarioLdapEntry;
import es.tenerife.secretaria.libro.repository.UsuarioLdapRepository;
import es.tenerife.secretaria.libro.service.LdapService;

@Service
@Transactional
public class LdapServiceImpl implements LdapService {

	private static final String WHERE_FILTER_UID = "uid";

	@Autowired
	private UsuarioLdapRepository usuarioLdapRepository;

	@Override
	public UsuarioLdapEntry buscarUsuarioLdap(String nombreUsuario) {
		LdapQuery query = LdapQueryBuilder.query().where(WHERE_FILTER_UID).is(nombreUsuario);
		return usuarioLdapRepository.findOne(query);
	}

}
