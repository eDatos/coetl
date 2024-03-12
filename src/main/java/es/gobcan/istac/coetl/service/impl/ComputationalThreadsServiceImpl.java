package es.gobcan.istac.coetl.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.time.Instant;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.config.QuartzConstants;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.job.PlatformExecutionJob;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.service.ComputationalThreadsService;
import es.gobcan.istac.coetl.service.validator.ComputationalThreadsValidator;
import es.gobcan.istac.coetl.util.CronUtils;

@Service
public class ComputationalThreadsServiceImpl implements ComputationalThreadsService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputationalThreadsService.class);
    //private static final String IDENTITY_JOB_PREFIX = "pentahoExecutionJob_";
    //private static final String IDENTITY_TRIGGER_PREFIX = "pentahoExectionTrigger_";

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Autowired
    private ComputationalThreadsValidator computationalThreadsValidator;

    //@Autowired
    //private SchedulerFactoryBean schedulerAccessorBean;

    private ComputationalThreads planifyAndSave(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to planify and save an Computational Thread : {}", computationalThreads);
        //JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + computationalThreads.getCode());
        final String executionPlanning = computationalThreads.getExecutionPlanning();

        CronExpression cronExpression = buildCronExpression(executionPlanning);
        Instant nextExecution = CronUtils.getNextExecutionFromCronExpression(cronExpression);
        computationalThreads.setNextExecution(nextExecution);
        //schedulePlatformExecutionJob(jobKey, cronExpression, computationalThreads);

        return save(computationalThreads);
    }
    
    private ComputationalThreads unplanifyAndSave(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to unplanify and save an Computational Thread : {}", computationalThreads);
        //JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + computationalThreads.getCode());
        //unschedulePentahoExecutionJob(jobKey);
        computationalThreads.setNextExecution(null);
        return save(computationalThreads);
    }

    private ComputationalThreads save(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to save an Computational Thread : {}", computationalThreads);
        return computationalThreadsRepository.saveAndFlush(computationalThreads);
    }

    @Override
    public ComputationalThreads create(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to create an Computational Thread : {}", computationalThreads);
        computationalThreadsValidator.validate(computationalThreads);
        return (computationalThreads.isPlanned()) ? planifyAndSave(computationalThreads) : save(computationalThreads);
    }

    private CronExpression buildCronExpression(final String executionPlanning) {
        try {
            return new CronExpression(executionPlanning);
        } catch (ParseException e) {
            final String message = String.format("The cron expression %s is not valid", executionPlanning);
            final String code = ErrorConstants.COMPUTATIONAL_THREAD_CRON_EXPRESSION_NOT_VALID;
            throw new CustomParameterizedExceptionBuilder().message(message).code(code, executionPlanning).build();
        }
    }

    @Override
    public ComputationalThreads findOne(Long id) {
        LOGGER.debug("Request to find an Computational Thread : {}", id);
        return computationalThreadsRepository.findOne(id);
    }

    /*
    private void schedulePlatformExecutionJob(JobKey jobKey, CronExpression cronExpression, ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to scheduled a new Quartz job : {}", jobKey.getName());
        //@formatter:off
        JobDetail job = newJob(PlatformExecutionJob.class)
                .withIdentity(jobKey)
                .usingJobData(QuartzConstants.COMPUTATIONAL_THREAD_CODE_JOB_DATA, computationalThreads.getCode())
                .build();

        CronTrigger trigger = newTrigger()
                .withIdentity(IDENTITY_TRIGGER_PREFIX + computationalThreads.getCode())
                .withSchedule(cronSchedule(cronExpression))
                .build();
        //@formatter:on

        try {
            deleteExistingJob(jobKey);
            schedulerAccessorBean.getScheduler().scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            final String message = String.format("Error during scheduling a new job %s", jobKey.getName());
            final String code = ErrorConstants.COMPUTATIONAL_THREAD_SCHEDULE_ERROR;
            CustomExceptionUtil.throwCustomParameterizedException(message, e, code);
        }
    }

    private void unschedulePentahoExecutionJob(JobKey jobKey) {
        LOGGER.debug("Request to unscheduled (if exists) a Quartz job : {}", jobKey.getName());

        try {
            deleteExistingJob(jobKey);
        } catch (SchedulerException e) {
            final String message = String.format("Error during unscheduling the job %s", jobKey.getName());
            final String code = ErrorConstants.COMPUTATIONAL_THREAD_UNSCHEDULE_ERROR;
            CustomExceptionUtil.throwCustomParameterizedException(message, e, code);
        }
    }

    private void deleteExistingJob(JobKey jobKey) throws SchedulerException {
        if (schedulerAccessorBean.getScheduler().checkExists(jobKey)) {
            schedulerAccessorBean.getScheduler().deleteJob(jobKey);
        }
    }
    */

}
