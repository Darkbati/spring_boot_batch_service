package com.gilbert.spring_boot_batch_service.core.listener;

import com.gilbert.spring_boot_batch_service.core.service.BatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContextRefreshEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private final BatchJobService batchJobService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        batchJobService.initializeBatchStatusRefresh();
    }
}
