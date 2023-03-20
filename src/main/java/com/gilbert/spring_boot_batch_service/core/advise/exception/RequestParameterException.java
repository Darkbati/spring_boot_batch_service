package com.gilbert.spring_boot_batch_service.advise.exception;

import com.gilbert.spring_boot_batch_service.advise.code.ErrorCode;

public class RequestParameterException extends AdviseBaseException {
    public RequestParameterException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RequestParameterException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
