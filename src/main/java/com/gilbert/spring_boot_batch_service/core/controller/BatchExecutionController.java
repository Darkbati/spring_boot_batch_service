package com.gilbert.spring_boot_batch_service.core.controller;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import com.gilbert.spring_boot_batch_service.dto.JobExecutionData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/execution", produces = "application/json")
@Tag(name = "Batch", description = "배치 잡 실행 API")
public class BatchExecutionController {
    private final BatchExecutionService batchExecutionService;

    @JsonManagedReference
    @PostMapping
    @Operation(summary = "배치 잡 실행", description = "배치 잡 실행 객체로 전달된 배치 잡 정보를 기반으로 배치 잡을 실행합니다.")
    public JobExecutionData executeJob(@RequestBody JobExecuter jobExecuter) throws Exception {
        return batchExecutionService.launch(jobExecuter);
    }

    @GetMapping("/name/{jobName}")
    @Operation(summary = "배치 잡 실행 전체 조회", description = "배치 잡 전체에 대해 실행 정보를 조회한 목록을 반환합니다.")
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
