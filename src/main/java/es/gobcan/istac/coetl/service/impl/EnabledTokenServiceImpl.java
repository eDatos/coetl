package es.gobcan.istac.coetl.service.impl;

import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.domain.EnabledTokenEntity;
import es.gobcan.istac.coetl.repository.EnabledTokenRepository;
import es.gobcan.istac.coetl.service.EnabledTokenService;

@Service
public class EnabledTokenServiceImpl implements EnabledTokenService {

    private final EnabledTokenRepository enabledTokenRepository;

    public EnabledTokenServiceImpl(EnabledTokenRepository enabledTokenRepository) {
        this.enabledTokenRepository = enabledTokenRepository;
    }

    @Override
    public void deleteByServiceTicket(String serviceTicket) {
        enabledTokenRepository.delete(serviceTicket);
    }

    @Override
    public boolean existsByToken(String token) {
        return enabledTokenRepository.existsByToken(token);
    }

    @Override
    public EnabledTokenEntity updateOrCreate(EnabledTokenEntity enabledTokenEntity) {
        return enabledTokenRepository.save(enabledTokenEntity);
    }
}
