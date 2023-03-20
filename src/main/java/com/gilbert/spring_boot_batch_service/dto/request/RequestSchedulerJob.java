package com.gilbert.spring_boot_batch_service.dto.request;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class RequestSchedulerJob {
    private String name;
    @Nullable
    private String description;
    @Nullable
    private String jobName;
    @Nullable
    private String cronSchedule;
    @Nullable
    private Map<String, Object> param;
}
