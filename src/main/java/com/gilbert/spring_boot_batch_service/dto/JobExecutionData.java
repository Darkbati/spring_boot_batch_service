package com.gilbert.spring_boot_batch_service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.batch.core.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
@Data
public class JobExecutionData {
    private Long id;
    private Integer version;
    private JobInstance jobInstance;
    private Collection<StepExecution> stepExecutions;
    private JobParameters jobParameters;
    private BatchStatus status;
    private ExitStatus exitStatus;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdated;
}
