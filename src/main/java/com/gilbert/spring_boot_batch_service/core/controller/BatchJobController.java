package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.dto.BatchJob;
import com.gilbert.spring_boot_batch_service.core.service.BatchJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
@Api(tags = "배치 잡 정보 조회 API", produces = MediaType.APPLICATION_JSON_VALUE)
public class BatchJobController {
    private final BatchJobService batchJobService;

    @GetMapping("/name/{jobName}")
    @ApiOperation(value = "배치 잡 정보 조회", notes = "배치 잡 이름에 해당하는 배치 잡 정보를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName",
                    value = "배치 잡 이름", defaultValue="", dataType = "string", paramType = "path", required = true)
    })
    public BatchJob get(@PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return batchJobService.job(jobName);
    }

    @GetMapping("/list")
    @ApiOperation(value = "배치 잡 전체 조회", notes = "배치 잡 전체 목록을 조회합니다.")
    public Collection<BatchJob> all() {
        return batchJobService.getJobList();
    }
}
