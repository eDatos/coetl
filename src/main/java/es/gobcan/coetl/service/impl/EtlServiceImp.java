package es.gobcan.coetl.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.job.PentahoExecutionJob;
import es.gobcan.coetl.repository.EtlRepository;
import es.gobcan.coetl.security.SecurityUtils;
import es.gobcan.coetl.service.EtlService;
import es.gobcan.coetl.service.PentahoExecutionService;
import es.gobcan.coetl.web.rest.util.QueryUtil;

@Service
public class EtlServiceImp implements EtlService {

    private static final Logger LOG = LoggerFactory.getLogger(EtlServiceImp.class);

    @Autowired
    EtlRepository etlRepository;

    @Autowired
    QueryUtil queryUtil;

    @Autowired
    PentahoExecutionService pentahoExecutionService;

    @Autowired
    private SchedulerFactoryBean schedulerAccessorBean;

    @Override
    public Etl create(Etl etl) {
        LOG.debug("Request to create an ETL : {}", etl);
        return save(etl);
    }

    @Override
    public Etl update(Etl etl) {
        LOG.debug("Request to update an ETL : {}", etl);
        return save(etl);
    }

    @Override
    public Etl delete(Etl etl) {
        LOG.debug("Request to delete an ETL : {}", etl);
        etl.setDeletedBy(SecurityUtils.getCurrentUserLogin());
        etl.setDeletionDate(ZonedDateTime.now());
        return save(etl);
    }

    @Override
    public Etl restore(Etl etl) {
        LOG.debug("Request to recover an ETL : {}", etl);
        etl.setDeletedBy(null);
        etl.setDeletionDate(null);
        return save(etl);
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
        pentahoExecutionService.execute(etl, Type.MANUAL);
    }

    private Etl save(Etl etl) {
        LOG.debug("Request to save an ETL : {}", etl);
        final String identityJobPrefix = "pentahoExecutionJob_";
        JobKey jobKey = new JobKey(identityJobPrefix + etl.getCode());
        if (etl.isPlanned()) {
            try {
                CronExpression cronExpression = new CronExpression(etl.getExecutionPlanning());
                etl.setNextExecution(getNextExecutionFromCronExpression(cronExpression));
                schedulePentahoExecutionJob(jobKey, cronExpression, etl);
            } catch (ParseException e) {
                //@formatter:off
                throw new CustomParameterizedExceptionBuilder()
                    .message(String.format("The cron expression %s is not valid", etl.getExecutionPlanning()))
                    .cause(e)
                    .code(ErrorConstants.ETL_CRON_EXPRESSION_NOT_VALID, etl.getExecutionPlanning())
                    .build();
                //@formatter:on
            }
        } else {
            etl.setNextExecution(null);
            checkAndUnschedulePentahoExecutionJob(jobKey);
        }

        if (etl.isDeleted() && etl.isPlanned()) {
            etl.setNextExecution(null);
            checkAndUnschedulePentahoExecutionJob(jobKey);
        }
        return etlRepository.save(etl);
    }

    private ZonedDateTime getNextExecutionFromCronExpression(CronExpression cronExpression) {
        Date now = Date.from(ZonedDateTime.now().toInstant());
        Date nextValidExecutionDate = cronExpression.getNextValidTimeAfter(now);
        return ZonedDateTime.ofInstant(nextValidExecutionDate.toInstant(), ZoneId.systemDefault());
    }

    private void schedulePentahoExecutionJob(JobKey jobKey, CronExpression cronExpression, Etl etl) {
        LOG.debug("Request to scheduled a new Quartz job : {}", jobKey.getName());
        final String identityTriggerPrefix = "pentahoExectionTrigger_";
        //@formatter:off
        JobDetail job = newJob(PentahoExecutionJob.class)
                .withIdentity(jobKey)
                .usingJobData(QuartzConstants.ETL_CODE_JOB_DATA, etl.getCode())
                .build();
        
        CronTrigger trigger = newTrigger()
                .withIdentity(identityTriggerPrefix + etl.getCode())
                .withSchedule(cronSchedule(cronExpression))
                .build();
        //@formatter:on

        try {
            if (!schedulerAccessorBean.getScheduler().checkExists(jobKey)) {
                schedulerAccessorBean.getScheduler().scheduleJob(job, trigger);
            } else {
                schedulerAccessorBean.getScheduler().deleteJob(jobKey);
                schedulerAccessorBean.getScheduler().scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            //@formatter:off
            throw new CustomParameterizedExceptionBuilder()
                .message(String.format("Error during schedule a new job %s", jobKey.getName()))
                .cause(e)
                .code(ErrorConstants.ETL_SCHEDULE_ERROR)
                .build();
            //@formatter:on
        }
    }

    private void checkAndUnschedulePentahoExecutionJob(JobKey jobKey) {
        LOG.debug("Request to unscheduled (if exists) a Quartz job : {}", jobKey.getName());

        try {
            if (schedulerAccessorBean.getScheduler().checkExists(jobKey)) {
                schedulerAccessorBean.getScheduler().deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            //@formatter:off
            throw new CustomParameterizedExceptionBuilder()
                .message(String.format("Error during unschedule the job %s", jobKey.getName()))
                .cause(e)
                .code(ErrorConstants.ETL_UNSCHEDULE_ERROR)
                .build();
            //@formatter:on
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
