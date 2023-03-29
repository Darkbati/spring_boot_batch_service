package com.gilbert.spring_boot_batch_service.core.scheduler.job;

import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
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
