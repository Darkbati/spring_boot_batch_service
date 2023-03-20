package com.gilbert.spring_boot_batch_service.core.controller;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gilbert.spring_boot_batch_service.core.advise.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advise.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import com.gilbert.spring_boot_batch_service.dto.JobExecutionData;
import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/execution", produces = "application/json")
public class BatchExecutionController {
    private final BatchExecutionService batchExecutionService;

    @JsonManagedReference
    @Operation(summary = "등록 배치 서비스 실행")
    @PostMapping
    public JobExecutionData executeJob(@RequestBody JobExecuter jobExecuter) throws Exception {
        return batchExecutionService.launch(jobExecuter);
    }

    @Operation(summary = "등록 배치명으로 이력 조회")
    @GetMapping("/name/{jobName}")
    public List<JobExecution> executionList(@PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return batchExecutionService.getExecutionList(jobName);
    }

    @Operation(summary = "등록 배치 ID로 이력 조회")
    @GetMapping("/id/{jobId}")
    public JobExecutionData jobExection(@PathVariable Long jobId) throws Exception {
        if (jobId == null || jobId <= 0) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return batchExecutionService.getExecutionDetail(jobId);
    }
}
