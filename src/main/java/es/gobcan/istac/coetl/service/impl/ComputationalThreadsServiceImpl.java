package es.gobcan.istac.coetl.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution;
import es.gobcan.istac.coetl.domain.ComputationalThreadExecution.Result;
import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.errors.util.CustomExceptionUtil;
import es.gobcan.istac.coetl.job.PlatformThreadExecutionJob;
import es.gobcan.istac.coetl.repository.ComputationalThreadsRepository;
import es.gobcan.istac.coetl.security.SecurityUtils;
import es.gobcan.istac.coetl.service.ComputationalThreadExecutionService;
import es.gobcan.istac.coetl.service.ComputationalThreadsService;
import es.gobcan.istac.coetl.service.ExecutionService;
import es.gobcan.istac.coetl.service.validator.ComputationalThreadsValidator;
import es.gobcan.istac.coetl.util.CronUtils;
import es.gobcan.istac.coetl.web.rest.util.QueryUtil;

@Service
public class ComputationalThreadsServiceImpl implements ComputationalThreadsService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputationalThreadsService.class);
    private static final int MAX_LENGHT_NOTES = 4000;
    private static final String MSG_ERROR_REGISTER_THREAD_ETLS_IN_HOP = "Error inesperado al registrar las ETLs del hilo computacional en Apache Hop: \n ";
    private static final String MSG_ERROR_REGISTER_THREAD_ETLS_EXECUTION = "Error inesperado al registrar la ejecución de las ETLs del hilo computacional";
    private static final String IDENTITY_JOB_PREFIX = "hopThreadExecutionJob_";
    private static final String IDENTITY_TRIGGER_PREFIX = "hopThreadExecutionTrigger_";

    @Autowired
    private ComputationalThreadsRepository computationalThreadsRepository;

    @Autowired
    private ComputationalThreadsValidator computationalThreadsValidator;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private ComputationalThreadExecutionService computationalThreadExecutionService;

    @Autowired
    private SchedulerFactoryBean schedulerAccessorBean;

    private ComputationalThreads save(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to save an Computational Thread : {}", computationalThreads);
        return computationalThreadsRepository.saveAndFlush(computationalThreads);
    }

    // ACTIONS
    @Override
    public ComputationalThreads create(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to create an Computational Thread : {}", computationalThreads);
        computationalThreadsValidator.validate(computationalThreads);
        return (computationalThreads.isPlanned()) ? planifyAndSave(computationalThreads) : save(computationalThreads);
    }

    @Override
    public ComputationalThreads update(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to update an Computational Thread : {}", computationalThreads);
        computationalThreadsValidator.validate(computationalThreads);
        return (computationalThreads.isPlanned()) ? planifyAndSave(computationalThreads) : unplanifyAndSave(computationalThreads);
    }

    @Override
    public ComputationalThreads delete(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to delete an Computational Thread : {}", computationalThreads);
        computationalThreads.setDeletedBy(SecurityUtils.getCurrentUserLogin());
        computationalThreads.setDeletionDate(Instant.now());

        return (computationalThreads.isPlanned()) ? unplanifyAndSave(computationalThreads) : save(computationalThreads);
    }

    @Override
    public ComputationalThreads restore(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to recover an Computational Thread : {}", computationalThreads);
        computationalThreads.setDeletedBy(null);
        computationalThreads.setDeletionDate(null);
        return (computationalThreads.isPlanned()) ? planifyAndSave(computationalThreads) : save(computationalThreads);
    }

    @Override
    public ComputationalThreads findOne(Long id) {
        LOGGER.debug("Request to find an Computational Thread : {}", id);
        return computationalThreadsRepository.findOne(id);
    }

    @Override
    public Page<ComputationalThreads> findAll(String query, boolean includeDeleted, Pageable pageable, String lastExecutionStartDate, String lastExecutionResult) {
        DetachedCriteria criteria = buildComputationalThreadCriteria(query, includeDeleted, pageable, lastExecutionStartDate, lastExecutionResult);
        return filteredListByRolOperationAllowed(computationalThreadsRepository.findAll(criteria, pageable));
    }

    private DetachedCriteria buildComputationalThreadCriteria(String query, boolean includeDeleted, Pageable pageable, String lastExecutionStartDate,
            String lastExecutionResult) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(query)) {
            queryBuilder.append(query);
        }
        queryBuilder.append(queryUtil.getQueryByLastExecution(lastExecutionStartDate, lastExecutionResult, queryBuilder));
        String finalQuery = getFinalQuery(includeDeleted, queryBuilder);
        return queryUtil.queryToComputationalThreadCriteria(pageable, finalQuery);
    }

    private String getFinalQuery(boolean includeDeleted, StringBuilder queryBuilder) {
        String finalQuery = queryBuilder.toString();
        if (BooleanUtils.isTrue(includeDeleted)) {
            finalQuery = queryUtil.queryIncludingDeleted(finalQuery);
        }
        return finalQuery;
    }

    private Page<ComputationalThreads> filteredListByRolOperationAllowed(Page<ComputationalThreads> computationalThreads) {
        List<ComputationalThreads> filtered = new ArrayList<>();
        if (!SecurityUtils.isAdmin()) {
            for (ComputationalThreads computationalThread : computationalThreads.getContent()) {
                if (computationalThread.getExternalItem() == null || SecurityUtils.haveAccessToOperationInRol(computationalThread.getExternalItem().getCode())) {
                    filtered.add(computationalThread);
                }
            }
            return new PageImpl<>(filtered);
        } else {
            return computationalThreads;
        }
    }

    // EXECUTIONS
    @Override
    public ComputationalThreadExecution createThreadExecution(ComputationalThreadExecution computationalThreadExecution, String executor) {
        computationalThreadExecution.setPlanningDate(Instant.now());
        computationalThreadExecution.setExecutor(executor);
        if (computationalThreadExecutionService.existsComputationalThreadExecutionByResultAndId(ComputationalThreadExecution.Result.RUNNING,
                computationalThreadExecution.getComputationalThread().getId())) {
            computationalThreadExecution.setResult(Result.DUPLICATED);
            computationalThreadExecution.setStartDate(null);
        } else if (Result.RUNNING.equals(computationalThreadExecution.getResult())) {
            computationalThreadExecution.setStartDate(Instant.now());
        }
        return computationalThreadExecutionService.create(computationalThreadExecution);
    }

    private String getRegisterErrorMsg(Execution resultExecution) {
        if (resultExecution.getResult().equals(Execution.Result.FAILED)) {
            return resultExecution.getNotes();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public void executeComputationalThread(ComputationalThreadExecution computationalThreadExecution, String executor) {
        List<Long> etlIds = computationalThreadExecution.getComputationalThread().getComputationalThreadsEtl().stream().filter(Objects::nonNull)
                .map(etl -> etl.getEtl().getId()).collect(Collectors.toList());
        if (!computationalThreadExecution.getResult().equals(Result.DUPLICATED)) {
            if (executionService.existsRunnnigOrWaitingByEtlIdIn(etlIds)) {
                computationalThreadExecution.setResult(Result.WAITING);
            } else {
                List<Etl> etls = computationalThreadExecution.getComputationalThread().getComputationalThreadsEtl().stream().filter(Objects::nonNull)
                        .map(etl -> etl.getEtl()).collect(Collectors.toList());
                List<Execution> registerExecutions = computationalThreadExecutionService.registerHopETL(etls, executor);
                execute(registerExecutions, etls, computationalThreadExecution);
            }
            computationalThreadExecutionService.update(computationalThreadExecution);
        }
    }

    private void execute(List<Execution> registerExecutions, List<Etl> etls, ComputationalThreadExecution computationalThreadExecution) {
        if (registerExecutions.size() != etls.size() || registerExecutions.stream().filter(exec -> Execution.Result.FAILED.equals(exec.getResult())).count() > 0) {
            StringBuilder msgError = new StringBuilder();
            for (Execution resultExecution : registerExecutions) {
                msgError.append(getRegisterErrorMsg(resultExecution));
                resultExecution.setResult(Execution.Result.FAILED);
            }
            computationalThreadExecutionFailed(registerExecutions, computationalThreadExecution,
                    StringUtils.substring(MSG_ERROR_REGISTER_THREAD_ETLS_IN_HOP.concat(msgError.toString()), 0, MAX_LENGHT_NOTES));
        } else {
            executeFirstETLInComputationalThread(computationalThreadExecution, registerExecutions);
        }
    }

    private void executeFirstETLInComputationalThread(ComputationalThreadExecution computationalThreadExecution, List<Execution> registerExecutions) {
        boolean isCreateIncorrect = computationalThreadExecutionService.createAllThreadETLExecutions(computationalThreadExecution, registerExecutions);
        if (isCreateIncorrect) {
            computationalThreadExecutionFailed(registerExecutions, computationalThreadExecution, MSG_ERROR_REGISTER_THREAD_ETLS_EXECUTION);
        } else {
            computationalThreadExecutionService.executeFirstEtlInThread(registerExecutions, computationalThreadExecution);
        }
    }

    private void computationalThreadExecutionFailed(List<Execution> registerExecutions, ComputationalThreadExecution computationalThreadExecution, String errorMessage) {
        computationalThreadExecutionService.unRegisterHopETL(registerExecutions);
        computationalThreadExecutionService.setThreadExecutionFailed(computationalThreadExecution, errorMessage);
    }

    // CRON
    private ComputationalThreads planifyAndSave(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to planify and save an Computational Thread : {}", computationalThreads);
        JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + computationalThreads.getCode());
        final String executionPlanning = computationalThreads.getExecutionPlanning();

        CronExpression cronExpression = buildCronExpression(executionPlanning);
        Instant nextExecution = CronUtils.getNextExecutionFromCronExpression(cronExpression);
        computationalThreads.setNextExecution(nextExecution);
        schedulePlatformExecutionJob(jobKey, cronExpression, computationalThreads);

        return save(computationalThreads);
    }

    private ComputationalThreads unplanifyAndSave(ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to unplanify and save an Computational Thread : {}", computationalThreads);
        JobKey jobKey = new JobKey(IDENTITY_JOB_PREFIX + computationalThreads.getCode());
        unschedulePentahoExecutionJob(jobKey);
        computationalThreads.setNextExecution(null);
        return save(computationalThreads);
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

    private void schedulePlatformExecutionJob(JobKey jobKey, CronExpression cronExpression, ComputationalThreads computationalThreads) {
        LOGGER.debug("Request to scheduled a new Quartz job : {}", jobKey.getName());
        //@formatter:off
        JobDetail job = newJob(PlatformThreadExecutionJob.class)
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

    @Override
    public ComputationalThreadExecution initDefaultExecutionCronJob(ComputationalThreads currentThread) {
        ComputationalThreadExecution newExecution = computationalThreadExecutionService.initDefaultExecutionCronJob();
        newExecution.setComputationalThread(currentThread);

        return newExecution;
    }

}
