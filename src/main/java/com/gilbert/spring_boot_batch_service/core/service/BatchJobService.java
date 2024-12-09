package com.gilbert.spring_boot_batch_service.core.service;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.dto.BatchJob;
import com.gilbert.spring_boot_batch_service.dto.BatchJobData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchJobService {
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;

    private final Map<String, BatchJobData> batchJobMap;
    private final JobRepository jobRepository;

    public void initializeBatchStatusRefresh() {
        try {
            List<String> jobs = jobExplorer.getJobNames();
            for (String job : jobs) {
                Set<JobExecution> runningJobs = jobExplorer.findRunningJobExecutions(job);

                for (JobExecution runningJob : runningJobs) {
                    try {
                        if (!runningJob.getStepExecutions().isEmpty()) {
                            Iterator<StepExecution> iter = runningJob.getStepExecutions().iterator();
                            while (iter.hasNext()) {
                                StepExecution stepExecution = iter.next();
                                if (stepExecution.getStatus().isRunning()) {
                                    stepExecution.setEndTime(LocalDateTime.now());
                                    stepExecution.setStatus(BatchStatus.FAILED);
                                    stepExecution.setExitStatus(new ExitStatus("FAILED", "BATCH JOB FAILED"));
                                    jobRepository.update(stepExecution);
                                }
                            }
                        }

                        runningJob.setEndTime(LocalDateTime.now());
                        runningJob.setStatus(BatchStatus.FAILED);
                        runningJob.setExitStatus(new ExitStatus("FAILED", "BATCH JOB FAILED"));
                        jobRepository.update(runningJob);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Collection<BatchJob> getJobList() {
        return jobRegistry.getJobNames().stream().map(n -> {
            return getJobDetail(n);
        }).collect(Collectors.toList());
    }

    public BatchJob job(String jobName) throws Exception {
        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        if (instance == null) {
            throw BatchJobException.NOT_FOUND_JOB;
        }

        return getJobDetail(jobName);
    }

    private BatchJob getJobDetail(String jobName) {
        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        BatchJobData batchJobData = batchJobMap.get(jobName);
        if (batchJobData == null) {
            batchJobData = BatchJobData.builder().jobName(jobName).description("").use(false).build();
        }

        if (instance == null) {
            return BatchJob.builder()
                    .name(jobName)
                    .description(batchJobData.getDescription())
                    .use(batchJobData.isUse())
                    .build();
        }

        JobExecution execution = jobExplorer.getJobExecution(instance.getInstanceId());
        if (execution == null) {
            return BatchJob.builder()
                    .name(jobName)
                    .description(batchJobData.getDescription())
                    .use(batchJobData.isUse())
                    .build();
        }

        return BatchJob.builder()
                .name(jobName)
                .description(batchJobData.getDescription())
                .use(batchJobData.isUse())
                .executeId(instance.getInstanceId())
                .executeLasted(execution.getEndTime())
                .parameters(execution.getJobParameters().getParameters())
                .build();
    }

    public Map<String, JobParameter<?>> getJobParameters(String jobName) {
        BatchJobData batchJobData = batchJobMap.get(jobName);
        if (batchJobData == null) {
            return null;
        }

        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        if (instance == null) {
            return null;
        }

        JobExecution jobExecution = jobExplorer.getJobExecution(instance.getInstanceId());
        return jobExecution.getJobParameters().getParameters();
    }
}
