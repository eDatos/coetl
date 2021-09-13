package es.gobcan.istac.coetl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.coetl.domain.EnabledTokenEntity;

import java.time.Instant;

@Repository
public interface EnabledTokenRepository extends JpaRepository<EnabledTokenEntity, String> {

    void deleteByExpirationDateBefore(Instant date);
    boolean existsByToken(String token);
}
