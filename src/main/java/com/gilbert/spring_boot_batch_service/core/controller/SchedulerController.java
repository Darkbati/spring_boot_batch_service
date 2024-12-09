package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.core.service.SchedulerService;
import com.gilbert.spring_boot_batch_service.dto.SchedulerDetail;
import com.gilbert.spring_boot_batch_service.dto.request.RequestSchedulerJob;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/scheduler", produces = "application/json")
@Tag(name = "Scheduler", description = "배치 스케줄 관리 API")
public class SchedulerController {

    private final SchedulerService schedulerService;

    private void requestBodyJobKey(String jobName, String jobGroup) throws Exception {
        if (!StringUtils.hasText(jobName)) {
            throw RequestParameterException.WRONG_PARAM;
        }

        if (!StringUtils.hasText(jobGroup)) {
            throw RequestParameterException.WRONG_PARAM;
        }
    }

    private void requestBodyValidate(RequestSchedulerJob requestSchedulerJob) throws Exception {
        if (!StringUtils.hasText(requestSchedulerJob.getName())) {
            throw RequestParameterException.WRONG_PARAM;
        }

        if (!StringUtils.hasText(requestSchedulerJob.getDescription())) {
            throw RequestParameterException.WRONG_PARAM;
        }

        if (!StringUtils.hasText(requestSchedulerJob.getCronSchedule())) {
            throw RequestParameterException.WRONG_PARAM;
        }
    }

    @PostMapping("/registry")
    @Operation(summary = "배치 스케줄 설정 등록", description = "배치 잡에 대한 스케줄을 등록합니다.")
    public SchedulerDetail scheduleRegistry(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        return schedulerService.addScheduler(requestSchedulerJob);
    }

    @DeleteMapping("/name/{jobName}/group/{jobGroup}")
    @Operation(summary = "배치 스케줄 설정 삭제", description = "배치 그룹에서 배치 잡이름에 해당하는 배치 잡을 삭제합니다.")
    public void schedulerRemove(@Parameter(name = "jobName", description = "Batch Job Name") @PathVariable("jobName") String jobName,
                                @Parameter(name = "jobGroup", description = "Batch Job Group") @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        schedulerService.removeScheduler(jobName, jobGroup);
    }

    @PostMapping("/modify")
    @Operation(summary = "배치 스케줄 설정 수정(기존 설정 삭제 후 추가)", description = "기준 배치 잡에 대한 스케줄 정보를 삭제 후 다시 등록합니다.")
    public SchedulerDetail schedulerModify(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        return schedulerService.modifyScheduler(requestSchedulerJob);
    }

    @PostMapping("/name/{jobName}/group/{jobGroup}/{statusType}")
    @Operation(summary = "배치 스케줄 설정[일시정지/재시작]", description = "배치 스케줄을 일시정지 또는 재시작 합니다.")
    public SchedulerDetail schedulerStatus(@Parameter(name = "jobName", description = "Batch Job Name") @PathVariable("jobName") String jobName,
                                           @Parameter(name = "jobGroup", description = "Batch Job Group") @PathVariable("jobGroup") String jobGroup,
                                           @Parameter(name = "statusType", description = "상태 - resume or pause") @PathVariable("statusType") String statusType) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        if (!StringUtils.hasText(statusType)) {
            throw RequestParameterException.WRONG_PARAM;
        }

        switch (statusType.toLowerCase()) {
            case "pause":
                schedulerService.schedulerPause(jobName, jobGroup);
                break;

            case "resume":
                schedulerService.schedulerResume(jobName, jobGroup);
                break;

            default:
                throw RequestParameterException.WRONG_PARAM;
        }

        return schedulerService.schedulerDetail(jobName, jobGroup);
    }

    @GetMapping("/list")
    @Operation(summary = "배치 스케줄 전체 목록 조회", description = "배치 스케줄 전체 목록을 조회합니다.")
    public List<SchedulerDetail> schedulerList() throws Exception {
        return schedulerService.schedulerList();
    }

    @GetMapping("/name/{jobName}/group/{jobGroup}/detail")
    @Operation(summary = "배치 스케줄 상세 조회", description = "배치 그룹에서 배치 이름에 해당하는 배치 잡의 상세 정보를 조회합니다.")
    public SchedulerDetail schedulerDetail(@Parameter(name = "jobName", description = "Batch Job Name") @PathVariable("jobName") String jobName,
                                           @Parameter(name = "jobGroup", description = "Batch Job Group") @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        return schedulerService.schedulerDetail(jobName, jobGroup);
    }
}
