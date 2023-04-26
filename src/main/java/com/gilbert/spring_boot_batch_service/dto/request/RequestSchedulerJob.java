package com.gilbert.spring_boot_batch_service.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Map;

@Data
@ApiModel
public class RequestSchedulerJob {
    @ApiModelProperty(value="Job 이름", dataType = "string", example = "IfpStatBatchJob")
    private String name;
    @Nullable
    @ApiModelProperty(value="Job 설명", dataType = "string", example = "대시보드 후원랭킹/IFP 통계 배치")
    private String description;
    @Nullable
    @ApiModelProperty(value="Cron 형식 스케줄", dataType = "string", example = "0 10 1 * * ? *")
    private String cronSchedule;
    @Nullable
    @ApiModelProperty(value="Job Parameters(JobDataMap)", dataType = "object",
            example = "{\"dateFormat\":\"yyyy-MM-dd\",\"dateFunction\":\"LocalDateTime.now().minusDays(1)\"}")
    private Map<String, Object> param;
}
