package com.gilbert.spring_boot_batch_service.advise.exception;

import com.gilbert.spring_boot_batch_service.advise.code.ErrorCode;

public class BatchJobException extends AdviseBaseException {
    public BatchJobException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BatchJobException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
