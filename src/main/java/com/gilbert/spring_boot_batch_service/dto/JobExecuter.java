package com.gilbert.spring_boot_batch_service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ApiModel
public class JobExecuter {
    @ApiModelProperty(value = "Job 이름", dataType = "string", example = "IfpStatBatchJob")
    private String name;
    @Singular("parameter")
    @ApiModelProperty(value="Job Parameters(JobDataMap)", dataType = "object",
            example = "{\"version\":\"1.0.0\",\"dateFormat\":\"yyyy-MM-dd\",\"dateFunction\":\"LocalDateTime.now().minusDays(1)\"}")
    private Map<String, Object> parameter = new HashMap<>();
}
