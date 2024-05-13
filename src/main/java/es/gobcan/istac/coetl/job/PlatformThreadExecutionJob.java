package es.gobcan.istac.coetl.job;

import java.time.Instant;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.config.QuartzConstants;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.util.CronUtils;

@Component
public class PlatformThreadExecutionJob extends AbstractCoetlQuartzJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformThreadExecutionJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Job Hop Computational Thread Execution running");
        executeComputationalThreadService(context);
    }

    private void executeComputationalThreadService(JobExecutionContext context) {
        String threadCode = (String) context.getJobDetail().getJobDataMap().get(QuartzConstants.COMPUTATIONAL_THREAD_CODE_JOB_DATA);
        PlatformTransactionManager platformTransactionManager = getPlatformTransactionManager(context);
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);

        try {
            transactionTemplate.execute(status -> {
                ComputationalThreads currentThread = getComputationalThreadsRepository(context).findOneByCode(threadCode);
                Instant nextExecution = CronUtils.getNextExecutionFromJobContext(context);
                currentThread.setNextExecution(nextExecution);
                getComputationalThreadsRepository(context).save(currentThread);
                ComputationalThreadExecution newExecution = getComputationalThreadsService(context).initDefaultExecutionCronJob(currentThread);
                ComputationalThreadExecution threadExecution = getComputationalThreadsService(context).createThreadExecution(newExecution, Constants.CRON_EXECUTOR_USER);
                getComputationalThreadsService(context).executeThread(threadExecution, Constants.CRON_EXECUTOR_USER);
                return true;
            });
        } catch(Exception e) {
            ComputationalThreads currentThread = getComputationalThreadsRepository(context).findOneByCode(threadCode);
            //getNotificationRestInternalFacade(context).sendExecutionErrorEtlNotice(currentThread);
            final String message = String.format("Error occurred during the execution. Computational Thread %s can not be executed", threadCode);
            final String code = ErrorConstants.ETL_EXECUTE_ERROR;
            CustomExceptionUtil.throwCustomParameterizedException(message, code);
        }

    }

}
