package com.gilbert.spring_boot_batch_service.core.advice.exception;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;

public class SchedulerCustomException extends AdviceBaseException {
    public static final SchedulerCustomException ALREADY_REGISTRY_JOB = new SchedulerCustomException(ErrorCode.ALREADY_REGISTRY_JOB);
    public static final SchedulerCustomException NOT_FOUND_SCHEDULER = new SchedulerCustomException(ErrorCode.NOT_FOUND_SCHEDULER);

    public SchedulerCustomException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SchedulerCustomException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
