package com.example.url.shortener.quartz;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UrlDeletionSchedulerTest {

    @Mock
    private Scheduler scheduler;

    private UrlDeletionScheduler urlDeletionScheduler;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        urlDeletionScheduler = new UrlDeletionScheduler(scheduler);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void scheduleUrlDeletion_shouldScheduleJob() throws SchedulerException {
        // Given
        long urlId = 1L;
        Instant deletionTime = Instant.now().plusSeconds(3600);

        // When
        urlDeletionScheduler.scheduleUrlDeletion(urlId, deletionTime);

        // Then
        verify(scheduler).scheduleJob(any(JobDetail.class), any(Trigger.class));
    }

    @Test
    void scheduleUrlDeletion_shouldCreateCorrectJobDetail() throws SchedulerException {
        // Given
        long urlId = 1L;
        Instant deletionTime = Instant.now().plusSeconds(3600);

        // When
        urlDeletionScheduler.scheduleUrlDeletion(urlId, deletionTime);

        // Then
        verify(scheduler).scheduleJob(argThat(jobDetail -> 
            jobDetail.getKey().getName().equals("urlDeletion-1") &&
            jobDetail.getJobDataMap().getLong("urlId") == 1L
        ), any(Trigger.class));
    }

    @Test
    void scheduleUrlDeletion_shouldCreateCorrectTrigger() throws SchedulerException {
        // Given
        long urlId = 1L;
        Instant deletionTime = Instant.now().plusSeconds(3600);

        // When
        urlDeletionScheduler.scheduleUrlDeletion(urlId, deletionTime);

        // Then
        verify(scheduler).scheduleJob(any(JobDetail.class), argThat(trigger -> 
            trigger.getKey().getName().equals("urlDeletionTrigger-1") &&
            trigger.getStartTime().equals(Date.from(deletionTime))
        ));
    }

    @Test
    void scheduleUrlDeletion_shouldThrowException_whenSchedulerFails() throws SchedulerException {
        // Given
        long urlId = 1L;
        Instant deletionTime = Instant.now().plusSeconds(3600);
        doThrow(new SchedulerException("Scheduler failed")).when(scheduler).scheduleJob(any(JobDetail.class), any(Trigger.class));

        // When & Then
        assertThrows(SchedulerException.class, () -> urlDeletionScheduler.scheduleUrlDeletion(urlId, deletionTime));
    }
}