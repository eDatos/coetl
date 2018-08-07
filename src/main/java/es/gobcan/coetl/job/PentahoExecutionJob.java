package es.gobcan.coetl.job;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import es.gobcan.coetl.config.QuartzConstants;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution.Type;

@Component
public class PentahoExecutionJob extends AbstractCoetlQuartzJob {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoExecutionJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Job Pentaho Execution running");
        executePentahoService(context);
    }

    private void executePentahoService(JobExecutionContext context) {
        String etlCode = (String) context.getJobDetail().getJobDataMap().get(QuartzConstants.ETL_CODE_JOB_DATA);
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager(context);
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);

        transactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                Etl currentEtl = getEtlRepository(context).findOneByCode(etlCode);
                currentEtl.setNextExecution(getNextExecutionFromContext(context));
                getEtlRepository(context).save(currentEtl);
                getPentahoExecutionService(context).execute(currentEtl, Type.AUTO);
                return true;
            }
        });
    }

    private ZonedDateTime getNextExecutionFromContext(JobExecutionContext context) {
        Date nextFireDate = context.getNextFireTime();
        return ZonedDateTime.ofInstant(nextFireDate.toInstant(), ZoneId.systemDefault());
    }

}
