package com.gilbert.spring_boot_batch_service.core.advice.exception;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;

public class RequestParameterException extends AdviceBaseException {

    public static final RequestParameterException WRONG_PARAM = new RequestParameterException(ErrorCode.WRONG_PARAM);
    public static final RequestParameterException RUNNING_JOB = new RequestParameterException(ErrorCode.RUNNING_JOB);
    public static final RequestParameterException NOT_EXECUTE_JOB = new RequestParameterException(ErrorCode.NOT_EXECUTE_JOB);

    public RequestParameterException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RequestParameterException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
