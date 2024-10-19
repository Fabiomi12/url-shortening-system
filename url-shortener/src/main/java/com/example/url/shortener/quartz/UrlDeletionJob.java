package com.example.url.shortener.quartz;

import com.example.url.shortener.dal.repo.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlDeletionJob implements Job {

    private final ShortenedUrlRepository shortenedUrlRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        var urlId = dataMap.getLong("urlId");

        try {
            shortenedUrlRepository.deleteById(urlId);
        } catch (Exception e) {
            throw new JobExecutionException("Failed to delete URL with ID: " + urlId, e);
        }
    }
}
