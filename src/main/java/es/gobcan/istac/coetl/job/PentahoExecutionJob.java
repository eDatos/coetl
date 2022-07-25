package es.gobcan.istac.coetl.job;

import java.time.Instant;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import es.gobcan.istac.coetl.config.QuartzConstants;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.util.CronUtils;

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

        try {
            transactionTemplate.execute(status -> {
                Etl currentEtl = getEtlRepository(context).findOneByCode(etlCode);
                Instant nextExecution = CronUtils.getNextExecutionFromJobContext(context);
                currentEtl.setNextExecution(nextExecution);
                getEtlRepository(context).save(currentEtl);
                Execution resultExecution = getPentahoExecutionService(context).execute(currentEtl, Type.AUTO);
                getExecutionService(context).create(resultExecution);
                return true;
            });
        }catch(Exception e){
            Etl currentEtl = getEtlRepository(context).findOneByCode(etlCode);
            getNotificationRestInternalFacade(context).sendExecutionErrorEtlNotice(currentEtl);
            final String message = String.format("Error occurred during the execution. ETL %s can not be executed", etlCode);
            final String code = ErrorConstants.ETL_EXECUTE_ERROR;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }
    }

}
