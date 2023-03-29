package com.gilbert.spring_boot_batch_service.core.scheduler.job;

import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

// @DisallowConcurrentExecution는 QuartzJob이 중복 실행되는 것을 예방하는 어노테이션
@Slf4j
@DisallowConcurrentExecution
public class ExecutorBatchJob extends QuartzJobBean {
    private BatchExecutionService batchExecutionService;

    public void setBatchExecutionService(BatchExecutionService batchExecutionService) {
        this.batchExecutionService = batchExecutionService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("################### RUN ###################");
    }
}
