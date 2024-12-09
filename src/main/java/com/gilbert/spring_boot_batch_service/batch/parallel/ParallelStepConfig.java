package com.gilbert.spring_boot_batch_service.batch.parallel;

import com.gilbert.spring_boot_batch_service.core.annotation.ServiceBatchJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ParallelStepConfig {
    private final String JOB_NAME = "Parralel_Job";

    @ServiceBatchJob(name = JOB_NAME, description = "")
    @Bean
    public Job parallelJob() {
        Flow flow1 = new FlowBuilder<Flow>("flow1")
                .start(parallelStep1())
                .next(parallelStep4())
                .build();

        Flow flow2 = new FlowBuilder<Flow>("flow2")
                .start(parallelStep1())
                .build();

        Flow flow3 = new FlowBuilder<Flow>("flow3")
                .start(parallelStep1())
                .build();

        Flow parallelStepFlow = new FlowBuilder<Flow>("parallelStepFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flow1, flow2, flow3)
                .build();

        return new JobBuilder(JOB_NAME)
                .start(parallelStepFlow)
                .build()
                .build();
    }

    @StepScope
    public Step parallelStep1() {
        return new StepBuilder("step1")
                .tasklet((contribution, chunkContext) -> {
                    for (int i = 1; i < 10; i++) {
                        log.info("[Step] : {}", i);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @StepScope
    public Step parallelStep2() {
        return new StepBuilder("step2")
                .tasklet((contribution, chunkContext) -> {
                    for (int i = 11; i < 21; i++) {
                        log.info("[Step] : {}", i);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @StepScope
    public Step parallelStep3() {
        return new StepBuilder("step3")
                .tasklet((contribution, chunkContext) -> {
                    for (int i = 21; i < 31; i++) {
                        log.info("[Step] : {}", i);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @StepScope
    public Step parallelStep4() {
        return new StepBuilder("step4")
                .tasklet((contribution, chunkContext) -> {
                    for (int i = 31; i < 41; i++) {
                        log.info("[Step] : {}", i);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
