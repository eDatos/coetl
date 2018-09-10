package es.gobcan.coetl.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.config.QuartzConstants;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.coetl.job.PentahoExecutionJob;
import es.gobcan.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.coetl.repository.EtlRepository;
import es.gobcan.coetl.security.SecurityUtils;
import es.gobcan.coetl.service.EtlService;
import es.gobcan.coetl.service.ExecutionService;
import es.gobcan.coetl.service.FileService;
import es.gobcan.coetl.web.rest.util.QueryUtil;

@Service
public class EtlServiceImpl implements EtlService {

    private static final Logger LOG = LoggerFactory.getLogger(EtlServiceImpl.class);
    private static final String IDENTITY_JOB_PREFIX = "pentahoExecutionJob_";
    private static final String IDENTITY_TRIGGER_PREFIX = "pentahoExectionTrigger_";

    @Autowired
    EtlRepository etlRepository;

    @Autowired
    QueryUtil queryUtil;

    @Autowired
    ExecutionService executionService;

    @Autowired
    PentahoExecutionService pentahoExecutionService;

    @Autowired
    FileService fileService;

    @Autowired
    private SchedulerFactoryBean schedulerAccessorBean;

    @Override
    public Etl create(Etl etl) {
        LOG.debug("Request to create an ETL : {}", etl);
        return (etl.isPlanned()) ? planifyAndSave(etl) : save(etl);
    }

    @Override
    public Etl update(Etl etl) {
        LOG.debug("Request to update an ETL : {}", etl);
        return (etl.isPlanned()) ? planifyAndSave(etl) : unplanifyAndSave(etl);
    }

    @Override
    public Etl delete(Etl etl) {
        LOG.debug("Request to delete an ETL : {}", etl);
        etl.setDeletedBy(SecurityUtils.getCurrentUserLogin());
        etl.setDeletionDate(Instant.now());

        return (etl.isPlanned()) ? unplanifyAndSave(etl) : save(etl);
    }

    @Override
    public Etl restore(Etl etl) {
        LOG.debug("Request to recover an ETL : {}", etl);
        etl.setDeletedBy(null);
        etl.setDeletionDate(null);
        return (etl.isPlanned()) ? planifyAndSave(etl) : save(etl);
    }

    @Override
    public Etl findOne(Long id) {
        LOG.debug("Request to find an ETL : {}", id);
        return etlRepository.findOne(id);
    }

    @Override
    public Page<Etl> findAll(String query, boolean includeDeleted, Pageable pageable) {
        LOG.debug("Request to find all ETLs by query : {}", query);
        DetachedCriteria criteria = buildEtlCriteria(query, includeDeleted, pageable);
        return etlRepository.findAll(criteria, pageable);
    }

    @Override
    public void execute(Etl etl) {
        LOG.debug("Request to execute ETL : {}", etl);
        Execution resultExecution = pentahoExecutionService.execute(etl, Type.MANUAL);
        executionService.create(resultExecution);

    }

    private Etl planifyAndSave(Etl etl) {
        LOG.debug("Request to planify and save an ETL : {}", etl);
        JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + etl.getCode());
        final String executionPlanning = etl.getExecutionPlanning();
        try {
            CronExpression cronExpression = new CronExpression(executionPlanning);
            etl.setNextExecution(getNextExecutionFromCronExpression(cronExpression));
            schedulePentahoExecutionJob(jobKey, cronExpression, etl);
        } catch (ParseException e) {
            final String message = String.format("The cron expression %s is not valid", executionPlanning);
            final String code = ErrorConstants.ETL_CRON_EXPRESSION_NOT_VALID;
            CustomExceptionUtil.throwCustomParameterizedException(message, e, code, executionPlanning);
        }

        return etlRepository.save(etl);
    }

    private Etl unplanifyAndSave(Etl etl) {
        LOG.debug("Request to unplanify and save an ETL : {}", etl);
        JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + etl.getCode());
        checkAndUnschedulePentahoExecutionJob(jobKey);
        etl.setNextExecution(null);
        return etlRepository.save(etl);
    }

    private Etl save(Etl etl) {
        LOG.debug("Request to save an ETL : {}", etl);
        return etlRepository.save(etl);
    }

    private Instant getNextExecutionFromCronExpression(CronExpression cronExpression) {
        Date now = Date.from(Instant.now());
        Date nextValidExecutionDate = cronExpression.getNextValidTimeAfter(now);
        return nextValidExecutionDate.toInstant();
    }

    private void schedulePentahoExecutionJob(JobKey jobKey, CronExpression cronExpression, Etl etl) {
        LOG.debug("Request to scheduled a new Quartz job : {}", jobKey.getName());
        //@formatter:off
        JobDetail job = newJob(PentahoExecutionJob.class)
                .withIdentity(jobKey)
                .usingJobData(QuartzConstants.ETL_CODE_JOB_DATA, etl.getCode())
                .build();
        
        CronTrigger trigger = newTrigger()
                .withIdentity(IDENTITY_TRIGGER_PREFIX + etl.getCode())
                .withSchedule(cronSchedule(cronExpression))
                .build();
        //@formatter:on

        try {
            deleteExistingJob(jobKey);
            schedulerAccessorBean.getScheduler().scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            final String message = String.format("Error during scheduling a new job %s", jobKey.getName());
            final String code = ErrorConstants.ETL_SCHEDULE_ERROR;
            CustomExceptionUtil.throwCustomParameterizedException(message, e, code);
        }
    }

    private void checkAndUnschedulePentahoExecutionJob(JobKey jobKey) {
        LOG.debug("Request to unscheduled (if exists) a Quartz job : {}", jobKey.getName());

        try {
            deleteExistingJob(jobKey);
        } catch (SchedulerException e) {
            final String message = String.format("Error during unscheduling the job %s", jobKey.getName());
            final String code = ErrorConstants.ETL_UNSCHEDULE_ERROR;
            CustomExceptionUtil.throwCustomParameterizedException(message, e, code);
        }
    }

    private void deleteExistingJob(JobKey jobKey) throws SchedulerException {
        if (schedulerAccessorBean.getScheduler().checkExists(jobKey)) {
            schedulerAccessorBean.getScheduler().deleteJob(jobKey);
        }
    }

    private DetachedCriteria buildEtlCriteria(String query, boolean includeDeleted, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(query)) {
            queryBuilder.append(query);
        }
        String finalQuery = getFinalQuery(includeDeleted, queryBuilder);
        return queryUtil.queryToEtlCriteria(pageable, finalQuery);
    }

    private String getFinalQuery(boolean includeDeleted, StringBuilder queryBuilder) {
        String finalQuery = queryBuilder.toString();
        if (BooleanUtils.isTrue(includeDeleted)) {
            finalQuery = queryUtil.queryIncludingDeleted(finalQuery);
        }
        return finalQuery;
    }

}
