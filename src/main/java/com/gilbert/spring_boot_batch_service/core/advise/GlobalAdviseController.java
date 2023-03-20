package com.gilbert.spring_boot_batch_service.advise;

import com.gilbert.spring_boot_batch_service.advise.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.advise.exception.RequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@RestControllerAdvice
public class GlobalAdviseController {
    @ExceptionHandler(value = {RequestParameterException.class})
    protected ResponseEntity<ErrorResponse> handlerRequestParameterException(RequestParameterException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.make(e);
    }

    @ExceptionHandler(value = {BatchJobException.class})
    protected ResponseEntity<ErrorResponse> BatchJobException(BatchJobException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.make(e);
    }

    @ExceptionHandler(value = {InvocationTargetException.class})
    protected ResponseEntity<ErrorResponse> InvocationTargetException(InvocationTargetException e) {
        log.error(e.getMessage(), e);
        return null;
    }
}
