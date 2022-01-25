package es.gobcan.istac.coetl.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import es.gobcan.istac.coetl.config.QuartzConstants;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.domain.ExternalItem;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.job.PentahoExecutionJob;
import es.gobcan.istac.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.istac.coetl.repository.EtlRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.service.EtlService;
import es.gobcan.istac.coetl.service.ExecutionService;
import es.gobcan.istac.coetl.service.ExternalItemService;
import es.gobcan.istac.coetl.service.validator.EtlValidator;
import es.gobcan.istac.coetl.util.CronUtils;
import es.gobcan.istac.coetl.web.rest.dto.EtlDTO;
import es.gobcan.istac.coetl.web.rest.util.QueryUtil;

@Service
public class EtlServiceImpl implements EtlService {

    private static final Logger LOG = LoggerFactory.getLogger(EtlService.class);
    private static final String IDENTITY_JOB_PREFIX = "pentahoExecutionJob_";
    private static final String IDENTITY_TRIGGER_PREFIX = "pentahoExectionTrigger_";

    @Autowired
    private EtlRepository etlRepository;

    @Autowired
    private EtlValidator etlValidator;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private PentahoExecutionService pentahoExecutionService;

    @Autowired
    private ExternalItemService externalItemService;

    @Autowired
    private SchedulerFactoryBean schedulerAccessorBean;

    @Override
    public Etl create(Etl etl) {
        LOG.debug("Request to create an ETL : {}", etl);
        etlValidator.validate(etl);
        createExternalItem(etl.getExternalItem());
        return (etl.isPlanned()) ? planifyAndSave(etl) : save(etl);
    }

    private void createExternalItem(ExternalItem externalItem){
        if(externalItem != null) {
            externalItemService.save(externalItem);
        }
    }
    @Override
    public Etl update(Etl etl) {
        LOG.debug("Request to update an ETL : {}", etl);
        etlValidator.validate(etl);
        createExternalItem(etl.getExternalItem());
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
        return filteredListByRolOperationAllowed(etlRepository.findAll(criteria, pageable));
    }

    @Override
    public void execute(Etl etl) {
        LOG.debug("Request to execute ETL : {}", etl);
        Execution resultExecution = pentahoExecutionService.execute(etl, Type.MANUAL);
        executionService.create(resultExecution);

    }

    @Override
    public boolean goingToChangeRepository(EtlDTO etlDto) {
        LOG.debug("Request to check if its going to change repository from DTO: {}", etlDto);
        if (etlDto.getId() == null) {
            return false;
        }
        Etl etl = etlRepository.findOne(etlDto.getId());
        if (etl.getUriRepository().equals(etlDto.getUriRepository())) {
            return false;
        }
        return true;
    }

    private Page<Etl> filteredListByRolOperationAllowed(Page<Etl> etls){
        List<Etl> filtered = new ArrayList<Etl>();
        if(!SecurityUtils.isAdmin()) {
            for (Etl etl : etls.getContent()) {
                if (etl.getExternalItem() == null ||
                    SecurityUtils.haveAccessToOperationInRol(etl.getExternalItem().getCode())){
                    filtered.add(etl);
                }
            }
            return new PageImpl<>(filtered);
        }else{
            return etls;
        }
    }

    private Etl planifyAndSave(Etl etl) {
        LOG.debug("Request to planify and save an ETL : {}", etl);
        JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + etl.getCode());
        final String executionPlanning = etl.getExecutionPlanning();

        CronExpression cronExpression = buildCronExpression(executionPlanning);
        Instant nextExecution = CronUtils.getNextExecutionFromCronExpression(cronExpression);
        etl.setNextExecution(nextExecution);
        schedulePentahoExecutionJob(jobKey, cronExpression, etl);

        return save(etl);
    }

    private CronExpression buildCronExpression(final String executionPlanning) {
        try {
            return new CronExpression(executionPlanning);
        } catch (ParseException e) {
            final String message = String.format("The cron expression %s is not valid", executionPlanning);
            final String code = ErrorConstants.ETL_CRON_EXPRESSION_NOT_VALID;
            throw new CustomParameterizedExceptionBuilder().message(message).code(code, executionPlanning).build();
        }
    }

    private Etl unplanifyAndSave(Etl etl) {
        LOG.debug("Request to unplanify and save an ETL : {}", etl);
        JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + etl.getCode());
        unschedulePentahoExecutionJob(jobKey);
        etl.setNextExecution(null);
        return save(etl);
    }

    private Etl save(Etl etl) {
        LOG.debug("Request to save an ETL : {}", etl);
        return etlRepository.saveAndFlush(etl);
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

    private void unschedulePentahoExecutionJob(JobKey jobKey) {
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
