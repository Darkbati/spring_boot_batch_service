package com.gilbert.spring_boot_batch_service.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;

@Slf4j
public class TaskletExceptionHandler implements ExceptionHandler {
    @Override
    public void handleException(RepeatContext context, Throwable throwable) throws Throwable {
        log.error(throwable.getMessage(), throwable);
    }
}
