package com.gilbert.spring_boot_batch_service.core.service;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.dto.BatchJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchJobService {
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;

    public Collection<BatchJob> getJobList() {
        return jobRegistry.getJobNames().stream().map(n -> {
            return getJobDetail(n);
        }).collect(toSet());
    }

    public BatchJob job(String jobName) throws Exception {
        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        if (instance == null) {
            throw new BatchJobException(ErrorCode.NOT_FOUND_JOB);
        }

        return getJobDetail(jobName);
    }

    private BatchJob getJobDetail(String jobName) {
        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        if (instance == null) {
            return BatchJob.builder()
                    .name(jobName)
                    .build();
        }

        JobExecution execution = jobExplorer.getJobExecution(instance.getInstanceId());
        return BatchJob.builder()
                .name(jobName)
                .executeId(instance.getInstanceId())
                .executeLasted(execution.getEndTime())
                .parameters(execution.getJobParameters().getParameters())
                .build();
    }
}
