package com.gilbert.spring_boot_batch_service.core.advise.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    WRONG_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
    NOT_FOUND_JOB(HttpStatus.NOT_FOUND, "등록된 JOB이 없습니다."),
    NOT_FOUND_SCHEDULER(HttpStatus.NOT_FOUND, "요청한 Scheduler가 없습니다.");

    private final HttpStatus httpStatus;
    private final String description;
}
