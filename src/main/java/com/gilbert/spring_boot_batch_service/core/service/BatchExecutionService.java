package com.gilbert.spring_boot_batch_service.core.service;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.core.common.JobParameterUtil;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import com.gilbert.spring_boot_batch_service.dto.JobExecutionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchExecutionService {
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;
    private final JobOperator jobOperator;
    private final TaskExecutor taskExecutor;

    private final static String RUN_ID = "run.id";

    public JobExecutionData launch(JobExecuter jobExecuter) throws Exception {
        return executionJob(jobExecuter);
    }

    @Async
    public void asyncLaunch(JobExecuter jobExecuter) throws Exception {
        executionJob(jobExecuter);
    }

    public boolean isRunning(String jobName) throws Exception {
        Set<JobExecution> runningJobs = jobExplorer.findRunningJobExecutions(jobName);
        for (JobExecution jobExecution : runningJobs) {
            if (jobExecution.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public void terminateJob(String jobName) throws Exception {
        try {
            Set<Long> executionId = jobOperator.getRunningExecutions(jobName);
            if (executionId.size() > 0) {
                jobOperator.stop(executionId.iterator().next());
            }
        } catch(Exception e) {
            throw BatchJobException.NOT_FOUND_JOB;
        }
    }

    public JobExecutionData executionJob(JobExecuter jobExecuter) throws Exception {
        Job job = jobRegistry.getJob(jobExecuter.getName());
        if (job == null) {
            throw BatchJobException.NOT_FOUND_JOB;
        }

        Map parameter = new LinkedHashMap<>(jobExecuter.getParameter());
        if (!parameter.containsKey(RUN_ID)) {
            Random rand = new Random(System.currentTimeMillis());
            String runId = String.format("%d", rand.nextInt(10000000));

            parameter.put(RUN_ID, runId);
        }

        JobParameters jobParameters = new JobParameters(JobParameterUtil.convertRawToParamMap(jobExecuter.getParameter()));
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        log.info("Starting {} with {} and Status {}", jobExecuter.getName(), jobExecuter, jobExecution.getStatus().name());

        return JobExecutionData.builder()
                .id(jobExecution.getId())
                .version(jobExecution.getVersion())
                .jobParameters(jobExecution.getJobParameters())
                .jobInstance(jobExecution.getJobInstance())
                .status(jobExecution.getStatus())
                .createTime(jobExecution.getCreateTime())
                .lastUpdated(jobExecution.getLastUpdated())
                .exitStatus(jobExecution.getExitStatus())
                .jobInstance(jobExecution.getJobInstance()).build();
    }

    public Long getJobInstanceId(String jobName) {
        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        if (instance == null) {
            return 0L;
        }

        return instance.getInstanceId();
    }

    // JobExecution List가 많으면 DB Timeout Exception 발생함
    @Deprecated
    public List<JobExecution> getExecutionList(String jobName) throws BatchJobException {
        JobInstance instance = jobExplorer.getLastJobInstance(jobName);
        if (instance == null) {
            throw BatchJobException.NOT_FOUND_JOB;
        }

        return jobExplorer.getJobExecutions(instance);
    }

    public JobExecutionData getExecutionDetail(Long jobId) throws BatchJobException {
        JobInstance instance = jobExplorer.getJobInstance(jobId);
        if (instance == null) {
            throw BatchJobException.NOT_FOUND_JOB;
        }

        JobExecution jobExecution = jobExplorer.getLastJobExecution(instance);
        return JobExecutionData.builder()
                .id(jobExecution.getId())
                .version(jobExecution.getVersion())
                .jobParameters(jobExecution.getJobParameters())
                .jobInstance(jobExecution.getJobInstance())
                .status(jobExecution.getStatus())
                .createTime(jobExecution.getCreateTime())
                .lastUpdated(jobExecution.getLastUpdated())
                .exitStatus(jobExecution.getExitStatus())
                .jobInstance(jobExecution.getJobInstance()).build();
    }
}
