package com.gilbert.spring_boot_batch_service.config;

import com.gilbert.spring_boot_batch_service.core.multipleExecutor.MultipleExecutionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    private final JobRegistry jobRegistry;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(threadPoolTaskExecutor);

        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        return jobLauncher;
    }

    @Bean
    public MultipleExecutionFactory multipleExecutionFactory() {
        MultipleExecutionFactory multipleExecutionFactory = new MultipleExecutionFactory();
        multipleExecutionFactory.setTaskExecutor(threadPoolTaskExecutor);

        return multipleExecutionFactory;
    }
}
