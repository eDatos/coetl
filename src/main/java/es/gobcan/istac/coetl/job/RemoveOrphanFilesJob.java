package es.gobcan.istac.coetl.job;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.service.FileService;

@Component
public class RemoveOrphanFilesJob {

    public static final Logger LOG = LoggerFactory.getLogger(RemoveOrphanFilesJob.class);

    @Autowired
    FileService fileService;

    @Scheduled(cron = Constants.REMOVE_ORPHAN_FILES_CRON)
    @Transactional
    public void execute() {
        LOG.info("Init job to remove orphan files");
        fileService.removeOrphans();
    }
}
