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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchExecutionService {
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;

    private final static String RUN_ID = "run.id";

    public JobExecutionData launch(JobExecuter jobExecuter) throws Exception {
        Job job = jobRegistry.getJob(jobExecuter.getName());
        if (job == null) {
            throw new BatchJobException(ErrorCode.NOT_FOUND_JOB);
        }

        if (!jobExecuter.getParameter().containsKey("version")) {
            jobExecuter.getParameter().put("version", this.getJobInstanceId(jobExecuter.getName()));
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
            throw new BatchJobException(ErrorCode.NOT_FOUND_JOB);
        }

        return jobExplorer.getJobExecutions(instance);
    }

    public JobExecutionData getExecutionDetail(Long jobId) throws BatchJobException {
        JobInstance instance = jobExplorer.getJobInstance(jobId);
        if (instance == null) {
            throw new BatchJobException(ErrorCode.NOT_FOUND_JOB);
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
