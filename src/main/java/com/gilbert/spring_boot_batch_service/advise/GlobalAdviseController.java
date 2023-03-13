package com.gilbert.spring_boot_batch_service.advise;

import com.gilbert.spring_boot_batch_service.advise.exception.BatchJobException;
import com.gilbert.spring_boot_batch_service.advise.exception.RequestParameterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;

@RestControllerAdvice
public class GlobalAdviseController {
    @ExceptionHandler(value = {RequestParameterException.class})
    protected ResponseEntity<ErrorResponse> handlerRequestParameterException(RequestParameterException e) {
        return ErrorResponse.make(e);
    }

    @ExceptionHandler(value = {BatchJobException.class})
    protected ResponseEntity<ErrorResponse> BatchJobException(BatchJobException e) {
        return ErrorResponse.make(e);
    }

    @ExceptionHandler(value = {InvocationTargetException.class})
    protected ResponseEntity<ErrorResponse> InvocationTargetException(InvocationTargetException e) {
        return null;
    }
}
