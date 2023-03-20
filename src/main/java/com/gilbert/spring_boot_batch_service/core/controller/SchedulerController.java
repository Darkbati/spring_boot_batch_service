package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advise.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advise.exception.RequestParameterException;
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
    public void scheduleRegistry(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        schedulerService.addScheduler(requestSchedulerJob);
    }

    @DeleteMapping("/name/{jobName}/group/{jobGroup}/remove")
    public void schedulerRemove(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        schedulerService.removeScheduler(jobName, jobGroup);
    }

    @PostMapping("/modify")
    public void schedulerModify(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        schedulerService.modifyScheduler(requestSchedulerJob);
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
