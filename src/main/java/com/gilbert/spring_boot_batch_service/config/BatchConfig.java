package com.gilbert.spring_boot_batch_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    private final int CORE_POOL_SIZE = 15;
    private final int MAX_POOL_SIZE = 25;
    private final int QUEUE_CAPACITY = 10;
    private final String THREAD_NAME_PREFIX = "async-task";

    public final JobRegistry jobRegistry;

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
