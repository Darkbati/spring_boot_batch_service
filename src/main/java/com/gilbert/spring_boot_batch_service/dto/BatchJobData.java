package com.gilbert.spring_boot_batch_service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BatchJobData {
    private String jobName;
    private String description;
    private boolean use;
}
