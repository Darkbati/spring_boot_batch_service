package com.gilbert.spring_boot_batch_service.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class SchedulerDetail {
    private String jobName;
    private String jobGroup;
    private String description;
    private Map jobDataMap;
    private Date startTime;
    private Date nextFireTime;
    private Date previousFireTime;
    private Integer priority;
}
