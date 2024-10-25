package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.core.service.BatchJobService;
import com.gilbert.spring_boot_batch_service.dto.BatchJob;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/job", produces = "application/json")
@Tag(name = "Batch Job", description = "배치 잡 정보 조회 API")
public class BatchJobController {
    private final BatchJobService batchJobService;

    @GetMapping("/name/{jobName}")
    @Operation(summary = "배치 잡 정보 조회", description = "배치 잡 이름에 해당하는 배치 잡 정보를 조회합니다.")
    public BatchJob get(@Parameter(name = "jobName", description = "Batch Job Name") @PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return batchJobService.job(jobName);
    }

    @GetMapping("/list")
    @Operation(summary = "배치 잡 전체 조회", description = "배치 잡 전체 목록을 조회합니다.")
    public Collection<BatchJob> all() {
        return batchJobService.getJobList();
    }
}
