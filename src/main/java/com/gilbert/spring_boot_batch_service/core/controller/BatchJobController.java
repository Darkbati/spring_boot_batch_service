package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.dto.BatchJob;
import com.gilbert.spring_boot_batch_service.core.service.BatchJobService;
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
public class BatchJobController {
    private final BatchJobService batchJobService;

    @GetMapping("/name/{jobName}")
    public BatchJob get(@PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return batchJobService.job(jobName);
    }

    @GetMapping("/list")
    public Collection<BatchJob> all() {
        return batchJobService.getJobList();
    }
}
