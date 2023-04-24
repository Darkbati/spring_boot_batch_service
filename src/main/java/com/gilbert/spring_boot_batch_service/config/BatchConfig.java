package com.gilbert.spring_boot_batch_service.config;

import com.gilbert.spring_boot_batch_service.dto.BatchJobData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    public final JobRegistry jobRegistry;

    @Bean(name = "batchJobMap")
    @Scope("singleton")
    public Map<String, BatchJobData> batchJobMap() {
        return new HashMap<>();
    }

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
