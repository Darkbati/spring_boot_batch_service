package com.gilbert.spring_boot_batch_service.batch.test;

import com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyCarJobConfig {
    private final String JOB_NAME = "MyCarJob";
    private final String JOB_FIRST_STEP = "MyCarJobFirstStep";
    private final String JOB_NEXT_STEP = "MyCarJobNextStep";

    @Bean
    public Tasklet myCarStartTasklet() {
        return new MyCarStartTasklet();
    }

    @ServiceBatchJob(name = JOB_NAME, description = "테스트")
    @Bean
    public Step myCarStartStep(JobRepository jobRepository, Tasklet myCarStartTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder(JOB_FIRST_STEP, jobRepository).tasklet(myCarStartTasklet, transactionManager).build();
    }

    @Bean
    public Job myCarJob(JobRepository jobRepository, Step myCarStartStep) {
        return new JobBuilder(JOB_NAME, jobRepository).start(myCarStartStep).build();
    }
}
