package com.gilbert.spring_boot_batch_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Batch Job 실행 객체")
public class JobExecuter {
    @Schema(description = "Batch Name", example = "myBatchJob")
    private String name;

    @Schema(description = "Batch Job Data Map", example = "{\"version\":\"1.0.0\",\"dateFormat\":\"yyyy-MM-dd\",\"dateFunction\":\"LocalDateTime.now().minusDays(1)\"}")
    private Map<String, Object> parameter = new HashMap<>();
}
