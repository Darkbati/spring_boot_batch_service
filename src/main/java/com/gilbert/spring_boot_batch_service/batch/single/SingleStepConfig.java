package com.gilbert.spring_boot_batch_service.batch.single;

import com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SingleStepConfig {
    private final String JOB_NAME = "Single_Job";
    private final String JOB_STEP_NAME = "Single_Step";
    private final String JOB_DESCRIPTION = "Single Job";

    private final SingleStep singleStep;

    /*
    @ServiceBatchJob(name = JOB_NAME, description = JOB_DESCRIPTION)
    @Bean
    public Job partitionJob(JobRepository jobRepository, Step partitiopnMasterStep, Step partitiopnTaskletStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(partitiopnMasterStep)
                .build();
    }

    @StepScope
    public Step step() {
        return new StepBuilder(JOB_STEP_NAME)
                .chunk(5)
                .reader(singleStep)
                .processor(singleStep)
                .writer(singleStep)
                .build();
    }
     */
}
