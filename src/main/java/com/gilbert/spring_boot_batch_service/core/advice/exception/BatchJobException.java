package com.gilbert.spring_boot_batch_service.core.advice.exception;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;

public class BatchJobException extends AdviceBaseException {

    public static final BatchJobException NOT_FOUND_JOB = new BatchJobException(ErrorCode.NOT_FOUND_JOB);

    public BatchJobException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BatchJobException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
