package com.gilbert.spring_boot_batch_service.batch.test2;

import com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob;
import com.gilbert.spring_boot_batch_service.dto.BatchData;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyShipJobConfig {
    private final String JOB_NAME = "MyShipJob";
    private final String JOB_STEP = "MyShipStep";

    @Bean
    public MyShipItemReader myShipReader() {
        return new MyShipItemReader();
    }

    @Bean
    public MyShipItemProcessor myShipItemProcessor() {
        return new MyShipItemProcessor();
    }

    @Bean
    public MyShipItemWriter myShipItemWriter() {
        return new MyShipItemWriter();
    }

    @ServiceBatchJob(name = JOB_NAME, description = "테스트2")
    @Bean
    public Step myShipStep(JobRepository jobRepository, Tasklet myTestTasklet, PlatformTransactionManager transactionManager, TaskExecutor taskExecutor) {
        return new StepBuilder(JOB_STEP, jobRepository)
                .<BatchData, BatchData>chunk(2, transactionManager)
                .reader(myShipReader())
                .processor(myShipItemProcessor())
                .writer(myShipItemWriter())
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Job myShipJob(JobRepository jobRepository, Step myShipStep) {
        return new JobBuilder(JOB_NAME, jobRepository).start(myShipStep).build();
    }
}
