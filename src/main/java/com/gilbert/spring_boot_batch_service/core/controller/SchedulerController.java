package com.gilbert.spring_boot_batch_service.core.controller;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import com.gilbert.spring_boot_batch_service.dto.SchedulerDetail;
import com.gilbert.spring_boot_batch_service.dto.request.RequestSchedulerJob;
import com.gilbert.spring_boot_batch_service.core.service.SchedulerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/scheduler", produces = "application/json")
@Api(tags = "배치 스케줄 관리 API", produces = MediaType.APPLICATION_JSON_VALUE)
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

        if (!StringUtils.hasText(requestSchedulerJob.getCronSchedule())) {
            throw new RequestParameterException(ErrorCode.WRONG_PARAM);
        }
    }

    @PostMapping("/registry")
    @ApiOperation(value = "배치 스케줄 설정 등록", notes = "배치 잡에 대한 스케줄을 등록합니다.")
    public SchedulerDetail scheduleRegistry(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        return schedulerService.addScheduler(requestSchedulerJob);
    }

    @DeleteMapping("/name/{jobName}/group/{jobGroup}")
    @ApiOperation(value = "배치 스케줄 설정 삭제", notes = "배치 그룹에서 배치 잡이름에 해당하는 배치 잡을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName",
                    value = "배치 잡 이름", defaultValue="", dataType = "string", paramType = "path", required = true),
            @ApiImplicitParam(name = "jobGroup",
                    value = "배치 잡 그룹 이름", defaultValue="IFLAND_JOB_GROUP", dataType = "string", paramType = "path", required = true)
    })
    public void schedulerRemove(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        schedulerService.removeScheduler(jobName, jobGroup);
    }

    @PostMapping("/modify")
    @ApiOperation(value = "배치 스케줄 설정 수정(기존 설정 삭제 후 추가)", notes = "기준 배치 잡에 대한 스케줄 정보를 삭제 후 다시 등록합니다.")
    public SchedulerDetail schedulerModify(@RequestBody RequestSchedulerJob requestSchedulerJob) throws Exception {
        requestBodyValidate(requestSchedulerJob);
        return schedulerService.modifyScheduler(requestSchedulerJob);
    }

    @PostMapping("/name/{jobName}/group/{jobGroup}/{statusType}")
    @ApiOperation(value = "배치 스케줄 설정[일시정지/재시작]", notes = "배치 스케줄을 일시정지 또는 재시작 합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobGroup",
                    value = "배치 잡 그룹 이름", defaultValue="IFLAND_JOB_GROUP", dataType = "string", paramType = "path", required = true),
            @ApiImplicitParam(name = "jobName",
                    value = "배치 잡 이름", defaultValue="", dataType = "string", paramType = "path", required = true),
            @ApiImplicitParam(name = "statusType",
                    value = "배치 상태(pause, resume)", defaultValue="pause", dataType = "string", paramType = "path", required = true)
    })
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
    @ApiOperation(value = "배치 스케줄 전체 목록 조회", notes = "배치 스케줄 전체 목록을 조회합니다.")
    public List<SchedulerDetail> schedulerList() throws Exception {
        return schedulerService.schedulerList();
    }

    @GetMapping("/name/{jobName}/group/{jobGroup}/detail")
    @ApiOperation(value = "배치 스케줄 상세 조회", notes = "배치 그룹에서 배치 이름에 해당하는 배치 잡의 상세 정보를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName",
                    value = "배치 잡 이름", defaultValue="", dataType = "string", paramType = "path", required = true),
            @ApiImplicitParam(name = "jobGroup",
                    value = "배치 잡 그룹 이름", defaultValue="IFLAND_JOB_GROUP", dataType = "string", paramType = "path", required = true)
    })
    public SchedulerDetail schedulerDetail(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup) throws Exception {
        requestBodyJobKey(jobName, jobGroup);
        return schedulerService.schedulerDetail(jobName, jobGroup);
    }
}
