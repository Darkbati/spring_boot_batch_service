package com.gilbert.spring_boot_batch_service.batch.partition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class PartitionMasterStep implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        return Map.of();
    }
}
