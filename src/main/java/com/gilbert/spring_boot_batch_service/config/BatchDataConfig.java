package com.gilbert.spring_boot_batch_service.config;

import com.gilbert.spring_boot_batch_service.dto.BatchJobData;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BatchDataConfig {
    @Bean(name = "expiringMap")
    @Scope("singleton")
    public Map<String, String> expiringMap() {
        return ExpiringMap.builder()
                .expirationPolicy(ExpirationPolicy.CREATED) // 맵에 삽입된 후
                .maxSize(10000)                             // 최대 10000개까지
                .expiration(1, TimeUnit.HOURS)   // 1시간 동안 보관
                .build();
    }

    @Bean(name = "batchJobMap")
    @Scope("singleton")
    public Map<String, BatchJobData> batchJobMap() {
        return new HashMap<>();
    }
}
