package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.EnabledTokenEntity;

public interface EnabledTokenService {

    void deleteByServiceTicket(String serviceTicket);
    boolean existsByToken(String token);
    EnabledTokenEntity updateOrCreate(EnabledTokenEntity enabledTokenEntity);
}
