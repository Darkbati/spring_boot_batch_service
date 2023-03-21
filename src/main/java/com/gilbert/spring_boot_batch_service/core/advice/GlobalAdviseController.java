package com.gilbert.spring_boot_batch_service.core.advice;

import com.gilbert.spring_boot_batch_service.core.advice.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.core.advice.exception.RequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@RestControllerAdvice
public class GlobalAdviseController {
    @ExceptionHandler(value = {RequestParameterException.class, BatchJobException.class})
    protected ResponseEntity<ErrorResponse> handlerRequestParameterException(RequestParameterException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.make(e);
    }

    @ExceptionHandler(value = {InvocationTargetException.class})
    protected ResponseEntity<ErrorResponse> InvocationTargetException(InvocationTargetException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.makeInternal(e);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponse> InvocationTargetException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.makeInternal(e);
    }
}
