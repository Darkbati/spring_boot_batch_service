package com.gilbert.spring_boot_batch_service.core.advice.exception;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;

public class SchedulerCustomException extends AdviseBaseException {
    public SchedulerCustomException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SchedulerCustomException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
