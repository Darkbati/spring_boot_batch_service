package com.gilbert.spring_boot_batch_service.controller;

import com.gilbert.spring_boot_batch_service.advise.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.advise.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.dto.BatchJob;
import com.gilbert.spring_boot_batch_service.service.JobService;
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
public class JobController {
    private final JobService jobService;

    @GetMapping("/name/{jobName}")
    public BatchJob get(@PathVariable String jobName) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return jobService.job(jobName);
    }

    @GetMapping("/list")
    public Collection<BatchJob> all() {
        return jobService.getJobList();
    }
}
