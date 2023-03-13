package com.gilbert.spring_boot_batch_service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.batch.core.JobParameter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class BatchJob {
    private String name;
    private Long executeId;
    private LocalDateTime executeLasted;
    private Map<String, JobParameter<?>> parameters;
}
