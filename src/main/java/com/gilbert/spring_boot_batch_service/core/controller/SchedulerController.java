package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.dto.SchedulerDetail;
import com.gilbert.spring_boot_batch_service.dto.request.RequestSchedulerJob;
import com.gilbert.spring_boot_batch_service.core.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/scheduler", produces = "application/json")
public class SchedulerController {

    private final SchedulerService schedulerService;

    private void requestBodyJobKey(String jobName, String jobGroup) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        if (!StringUtils.hasText(jobGroup)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }
    }

    private void requestBodyValidate(RequestSchedulerJob requestSchedulerJob) throws Exception {
        if (!StringUtils.hasText(requestSchedulerJob.getName())) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        if (!StringUtils.hasText(requestSchedulerJob.getDescription())) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        if (!StringUtils.hasText(requestSchedulerJob.getJobName())) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        if (!StringUtils.hasText(requestSchedulerJob.getCronSchedule())) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }
    }

    @PostMapping("/registry")
    public SchedulerDetail scheduleRegistry(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        return schedulerService.addScheduler(requestSchedulerJob);
    }

    @DeleteMapping("/name/{jobName}/group/{jobGroup}")
    public void schedulerRemove(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        schedulerService.removeScheduler(jobName, jobGroup);
    }

    @PostMapping("/modify")
    public SchedulerDetail schedulerModify(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        return schedulerService.modifyScheduler(requestSchedulerJob);
    }

    @PostMapping("/name/{jobName}/group/{jobGroup}/{statusType}")
    public SchedulerDetail schedulerStatus(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup, @PathVariable("statusType") String statusType) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        if (!StringUtils.hasText(statusType)) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        switch (statusType.toLowerCase()) {
            case "pause":
                schedulerService.schedulerPause(jobName, jobGroup);
                break;

            case "resume":
                schedulerService.schedulerResume(jobName, jobGroup);
                break;

            default:
                throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }

        return schedulerService.schedulerDetail(jobName, jobGroup);
    }

    @GetMapping("/list")
    public List<SchedulerDetail> schedulerList() throws Exception {
        return schedulerService.schedulerList();
    }

    @GetMapping("/name/{jobName}/group/{jobGroup}/detail")
    public SchedulerDetail schedulerDetail(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        return schedulerService.schedulerDetail(jobName, jobGroup);
    }
}
