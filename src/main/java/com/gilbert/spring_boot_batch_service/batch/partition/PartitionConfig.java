package com.gilbert.spring_boot_batch_service.batch.partition;

import com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob;
import com.gilbert.spring_boot_batch_service.dto.BatchData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PartitionConfig {
    private final String JOB_NAME = "PartitionJob";
    private final String JOB_DESCRIPTION = "Partition Job";
    private final String JOB_FIRST_STEP = "PartitionMasterStep";
    private final String JOB_SUB_STEP = "PartitionWorker";
    private final String JOB_NEXT_STEP = "PartitionTaskletStep";

    private final PartitionMasterStep partitionMasterStep;
    private final PartitionWorker partitionWorker;
    private final TaskletStep taskletStep;

    private final TaskExecutor taskExecutor;

    @ServiceBatchJob(name = JOB_NAME, description = JOB_DESCRIPTION)
    @Bean
    public Job partitionJob(JobRepository jobRepository, Step partitiopnMasterStep, Step partitiopnTaskletStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(partitiopnMasterStep)
                .next(partitiopnTaskletStep)
                .build();
    }

    @Bean
    public Step partitiopnMasterStep(JobRepository jobRepository, Step partitiopnWorkerStep) {
        return new StepBuilder(JOB_SUB_STEP, jobRepository)
                .partitioner(partitiopnWorkerStep.getName(), partitionMasterStep)
                .step(partitiopnWorkerStep)
                .gridSize(3)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step partitiopnWorkerStep(JobRepository jobRepository) {
        return new StepBuilder(JOB_FIRST_STEP, jobRepository)
                .<List<BatchData>, List<BatchData>>chunk(5)
                .reader(partitionWorker)
                .writer(partitionWorker)
                .build();
    }

    @Bean
    public Step partitiopnTaskletStep(JobRepository jobRepository) {
        return new StepBuilder(JOB_NEXT_STEP, jobRepository)
                .tasklet(taskletStep)
                .build();
    }
}
