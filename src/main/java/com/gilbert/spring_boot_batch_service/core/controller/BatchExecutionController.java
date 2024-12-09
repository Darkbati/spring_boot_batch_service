package com.gilbert.spring_boot_batch_service.core.controller;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.core.multipleExecutor.MultipleExecutionFactory;
import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import com.gilbert.spring_boot_batch_service.dto.JobExecutionData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.http.MediaType;
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
    private final MultipleExecutionFactory multipleExecutionFactory;

    @JsonManagedReference
    @PostMapping("/{executionType}")
    @Operation(summary = "배치 잡 실행", description = "배치 잡 실행 객체로 전달된 배치 잡 정보를 기반으로 배치 잡을 실행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public JobExecutionData executeJob(@PathVariable String executionType, @RequestBody JobExecuter jobExecuter) throws Exception {
        if (!StringUtils.hasText(executionType)) {
            throw RequestParameterException.WRONG_PARAM;
        }

        if (batchExecutionService.isRunning(jobExecuter.getName())) {
            throw RequestParameterException.RUNNING_JOB;
        }

        executionType = executionType.toUpperCase();
        if (executionType.equals("async")) {
            if (multipleExecutionFactory != null && multipleExecutionFactory.contains(jobExecuter.getName())) {
                multipleExecutionFactory.execution(jobExecuter);
                return null;
            }

            batchExecutionService.asyncLaunch(jobExecuter);
            return null;
        }

        if (multipleExecutionFactory != null && multipleExecutionFactory.contains(jobExecuter.getName())) {
            throw RequestParameterException.NOT_EXECUTE_JOB;
        }

        return batchExecutionService.launch(jobExecuter);
    }

    @DeleteMapping("/terminate/{jobName}")
    public void terminateJob(@PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw RequestParameterException.WRONG_PARAM;
        }

        batchExecutionService.terminateJob(jobName);
    }

    @Deprecated
    @GetMapping("/name/{jobName}")
    @Operation(summary = "배치 잡 실행 전체 조회", description = "배치 잡 전체에 대해 실행 정보를 조회한 목록을 반환합니다.")
    public List<JobExecution> executionList(@Parameter(name = "jobName", description = "Batch Job Name") @PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw RequestParameterException.WRONG_PARAM;
        }

        return batchExecutionService.getExecutionList(jobName);
    }

    @Operation(summary = "등록 배치 ID로 이력 조회")
    @GetMapping("/id/{jobId}")
    public JobExecutionData jobExection(@Parameter(name = "jobId", description = "실행된 배치 Instance Id") @PathVariable Long jobId) throws Exception {
        if (jobId == null || jobId <= 0) {
            throw RequestParameterException.WRONG_PARAM;
        }

        return batchExecutionService.getExecutionDetail(jobId);
    }
}
