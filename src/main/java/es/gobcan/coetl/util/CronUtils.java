package es.gobcan.coetl.util;

import java.time.Instant;
import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.JobExecutionContext;

public final class CronUtils {

    private CronUtils() {

    }

    public static Instant getNextExecutionFromCronExpression(CronExpression cronExpression) {
        Date now = Date.from(Instant.now());
        Date nextValidExecutionDate = cronExpression.getNextValidTimeAfter(now);
        return nextValidExecutionDate.toInstant();
    }

    public static Instant getNextExecutionFromJobContext(JobExecutionContext context) {
        Date nextFireDate = context.getNextFireTime();
        return nextFireDate.toInstant();
    }
}
