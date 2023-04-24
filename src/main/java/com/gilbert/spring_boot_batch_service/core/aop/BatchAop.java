package com.gilbert.spring_boot_batch_service.core.aop;

import com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob;
import com.gilbert.spring_boot_batch_service.dto.BatchJobData;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(1)
@Aspect
@Scope("singleton")
@Component
@RequiredArgsConstructor
public class BatchAop {
    private final Map<String, BatchJobData> batchJobMap;

    @Around("@annotation(com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob)")
    public synchronized Object collectorBatchJob(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        ServiceBatchJob custom = methodSignature.getMethod().getAnnotation(ServiceBatchJob.class);

        // 정상 어노테이션만 등록한다.
        if (!custom.name().equals("UNKNOWN_BATCH")) {
            if (!batchJobMap.containsKey(custom.name())) {
                batchJobMap.put(custom.name(), BatchJobData.builder().jobName(custom.name()).description(custom.description()).use(custom.use()).build());
            }
        }

        Object proceedReturnValue = proceedingJoinPoint.proceed();
        return proceedReturnValue;
    }
}
