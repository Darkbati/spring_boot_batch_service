package com.gilbert.spring_boot_batch_service.core.advice.exception;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import lombok.Getter;

@Getter
public class AdviseBaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public AdviseBaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public AdviseBaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
