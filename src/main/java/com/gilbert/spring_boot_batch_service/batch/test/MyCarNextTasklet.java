package com.gilbert.spring_boot_batch_service.batch.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class MyCarNextTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("###########################################################################");
        log.info("=====> Next ");
        log.info("###########################################################################");
        return RepeatStatus.FINISHED;
    }
}
