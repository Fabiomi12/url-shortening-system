package com.example.url.shortener.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UrlDeletionScheduler {

    private final Scheduler scheduler;

    public void scheduleUrlDeletion(long urlId, Instant deletionTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(UrlDeletionJob.class)
                .withIdentity("urlDeletion-" + urlId)
                .usingJobData("urlId", urlId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("urlDeletionTrigger-" + urlId)
                .startAt(Date.from(deletionTime))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withMisfireHandlingInstructionFireNow())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
