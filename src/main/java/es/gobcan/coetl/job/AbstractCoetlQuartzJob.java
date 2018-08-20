package es.gobcan.coetl.job;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.PlatformTransactionManager;

import es.gobcan.coetl.config.QuartzConstants;
import es.gobcan.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.coetl.repository.EtlRepository;
import es.gobcan.coetl.service.ExecutionService;

public abstract class AbstractCoetlQuartzJob extends QuartzJobBean {

    protected PlatformTransactionManager getPlatformTransactionManager(JobExecutionContext context) {
        return getApplicationContext(context).getBean(PlatformTransactionManager.class);
    }

    private ApplicationContext getApplicationContext(JobExecutionContext context) {
        try {
            return (ApplicationContext) context.getScheduler().getContext().get(QuartzConstants.APPLICATION_CONTEXT_KEY);
        } catch (SchedulerException e) {
            String etlCode = (String) context.getJobDetail().getJobDataMap().get(QuartzConstants.ETL_CODE_JOB_DATA);
            //@formatter:off
            throw new CustomParameterizedExceptionBuilder()
                .message("Error during Quartz job execution")
                .cause(e)
                .code(ErrorConstants.QUARTZ_JOB_EXECUTION_ERROR, etlCode)
                .build();
            //@formatter:on
        }
    }

    protected PentahoExecutionService getPentahoExecutionService(JobExecutionContext context) {
        return getApplicationContext(context).getBean(PentahoExecutionService.class);
    }

    protected EtlRepository getEtlRepository(JobExecutionContext context) {
        return getApplicationContext(context).getBean(EtlRepository.class);
    }

    protected ExecutionService getExecutionService(JobExecutionContext context) {
        return getApplicationContext(context).getBean(ExecutionService.class);
    }
}
