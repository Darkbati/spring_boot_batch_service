package com.gilbert.spring_boot_batch_service.core.service;

import com.gilbert.spring_boot_batch_service.core.advise.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advise.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.core.common.JobParameterUtil;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import com.gilbert.spring_boot_batch_service.dto.JobExecutionData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchExecutionService {
    private final JobRepository jobRepository;
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private JobLauncher jobLauncher = null;

    private final static String RUN_ID = "run.id";

    @PostConstruct
    private void init() {
        this.jobLauncher = jobLauncher(new SimpleAsyncTaskExecutor(threadPoolTaskExecutor), jobRepository);
    }

    private JobLauncher jobLauncher(TaskExecutor taskExecutor, JobRepository jobRepository) {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        return jobLauncher;
    }

    public JobExecutionData launch(JobExecuter jobExecuter) throws Exception {
        Job job = jobRegistry.getJob(jobExecuter.getName());
        if (job == null) {
            throw new BatchJobException(ErrorCode.NOT_FOUND_JOB);
        }

        // 아래 RUN_ID 파라메터가 중복실행 가능하도록 만들어져 있다.
        jobExecuter.getParameter().put(RUN_ID, new Date().getTime());
        JobParameters jobParameters = new JobParameters(JobParameterUtil.convertRawToParamMap(jobExecuter.getParameter()));
        log.info("Starting {} with {}", jobExecuter.getName(), jobExecuter);

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
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
