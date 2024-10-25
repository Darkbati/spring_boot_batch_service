package com.gilbert.spring_boot_batch_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Map;

@Data
@Schema(description = "Scheduler Job 객체")
public class RequestSchedulerJob {
    @Schema(description = "Job 이름", type = "string", example = "myBatchJob")
    private String name;

    @Nullable
    @Schema(description = "Job 설명", type = "string", example = "대시보드 후원랭킹/IFP 통계 배치")
    private String description;

    @Nullable
    @Schema(description = "Cron 형식 스케줄", type = "string", example = "0 10 1 * * ? *\"")
    private String cronSchedule;

    @Nullable
    @Schema(description = "Job Parameters(JobDataMap)", type = "object", example = "{\"dateFormat\":\"yyyy-MM-dd\",\"dateFunction\":\"LocalDateTime.now().minusDays(1)\"}")
    private Map<String, Object> param;
}
