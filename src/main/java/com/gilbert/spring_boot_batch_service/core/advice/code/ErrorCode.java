package com.gilbert.spring_boot_batch_service.core.advice.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    WRONG_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    NOT_FOUND_JOB(HttpStatus.NOT_FOUND, "등록된 JOB이 없습니다."),
    ALREADY_REGISTRY_JOB(HttpStatus.FOUND, "이미 등록된 JOB이 있습니다."),
    NOT_FOUND_SCHEDULER(HttpStatus.NOT_FOUND, "요청한 Scheduler가 없습니다."),
    RUNNING_JOB(HttpStatus.BAD_REQUEST, "이 JOB은 구동 중입니다. JOB 상태 확인 후 요청해주세요."),
    NOT_EXECUTE_JOB(HttpStatus.BAD_REQUEST, "반복 JOB 실행은 비동기 실행만 가능합니다.");

    private final HttpStatus httpStatus;
    private final String description;
}
