package es.gobcan.istac.coetl.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.repository.EnabledTokenRepository;

import javax.transaction.Transactional;
import java.time.Instant;

@Component
public class DeleteExpiredEnabledTokens {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteExpiredEnabledTokens.class);
    private final EnabledTokenRepository enabledTokenRepository;

    public DeleteExpiredEnabledTokens(EnabledTokenRepository enabledTokenRepository) {
        this.enabledTokenRepository = enabledTokenRepository;
    }

    @Transactional
    @Scheduled(cron = "${application.jobs.cron.enabledTokens}")
    public void execute() {
        LOG.info("Starting to clean expired enabled tokens...");
        enabledTokenRepository.deleteByExpirationDateBefore(Instant.now());
        LOG.info("All expired enabled tokens deleted");
    }
}
