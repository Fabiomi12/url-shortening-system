package com.example.url.shortener.quartz;

import com.example.url.shortener.dal.repo.ShortenedUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UrlDeletionJobTest {

    @Mock
    private ShortenedUrlRepository shortenedUrlRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private JobDetail jobDetail;

    private UrlDeletionJob urlDeletionJob;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        urlDeletionJob = new UrlDeletionJob(shortenedUrlRepository);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("urlId", 123L);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void execute_shouldDeleteUrl() throws JobExecutionException {
        urlDeletionJob.execute(jobExecutionContext);

        verify(shortenedUrlRepository).deleteById(123L);
    }

    @Test
    void execute_shouldThrowJobExecutionException_whenDeleteFails() {
        doThrow(new RuntimeException("Delete failed")).when(shortenedUrlRepository).deleteById(anyLong());

        assertThrows(JobExecutionException.class, () -> urlDeletionJob.execute(jobExecutionContext));
    }
}