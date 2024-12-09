package com.gilbert.spring_boot_batch_service.core.listener;

import com.slack.api.Slack;
import com.slack.api.model.block.*;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BatchJobExecutionListener implements JobExecutionListener {
    protected final String PREFIX_PROFILE = "{PROFILE}";
    protected final String PREFIX_JOB_NAME = "{JOB_NAME}";
    protected final String PREFIX_DURATION = "{SECOND}";
    protected final String PREFIX_BASE_TIME = "{BASE_TIME}";

    protected final String SLACK_TITLE_MSG = "({PROFILE}) IFLAND Batch Service (JOB : {JOB_NAME})";
    protected final String DURATION_TIME_MSG = "Duration of time : {SECOND}(s)";
    protected final String JOB_TIME_OUT_MSG = "Operation '{JOB_NAME}' has timed out. cut-off time ({BASE_TIME})";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${slack.webhook.uri}")
    private String slackUri;

    @Value("${slack.batch.cutOffTime:600000}")
    private Long cutOffTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("### Job name : {} Start ###", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        long startTime = jobExecution.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endTime = jobExecution.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long executionTime = endTime - startTime;

        String title = SLACK_TITLE_MSG.replace(PREFIX_PROFILE, activeProfile.toUpperCase()).replace(PREFIX_JOB_NAME, jobName);
        if (jobExecution.getAllFailureExceptions().size() > 0) {
            // Exception 발생한 경우만 Notify
            Throwable throwable = jobExecution.getAllFailureExceptions().get(0);

            String duration = DURATION_TIME_MSG.replace(PREFIX_DURATION, Long.toString(executionTime / 1000));
            if (StringUtils.hasText(activeProfile) && (activeProfile.toUpperCase().equals("PROD") || activeProfile.toUpperCase().equals("STG"))) {
                this.sendMessage(this.slackUri, title, throwable.getMessage(), duration);
            }
        } else {
            // 시간 초과한 경우만 Notify
            if (executionTime > cutOffTime) {
                String message = JOB_TIME_OUT_MSG.replace(PREFIX_JOB_NAME, jobName).replace(PREFIX_BASE_TIME, Long.toString(cutOffTime / 1000));
                if (StringUtils.hasText(activeProfile) && (activeProfile.toUpperCase().equals("PROD") || activeProfile.toUpperCase().equals("STG"))) {
                    this.sendMessage(this.slackUri, title, message);
                }
            }
        }

        log.info("### job name : {} end, execution time : {}", jobName, executionTime);
    }

    /* Slack Notify 조건 정리
    1. Exception 이 1개 이상인 경우.
    2. 처리 시간이 기준시간을 초과한 경우.
   */
    private void sendMessage(String url, String title, String bodyMessage, String durationMessage) {
        this.slackSend(url, title, bodyMessage, durationMessage);
    }

    private void sendMessage(String url, String title, String bodyMessage) {
        this.slackSend(url, title, bodyMessage, null);
    }

    protected void slackSend(String url, String title, String bodyMessage, String durationMessage) {
        try {
            List<LayoutBlock> blocks = new ArrayList<>();
            // Title 설정
            blocks.add(HeaderBlock.builder()
                    .text(PlainTextObject.builder().text(title).build())
                    .build());

            // Divide Line
            blocks.add(DividerBlock.builder().build());

            // Exception Message 또는 Over Time 관련 메시지
            blocks.add(SectionBlock.builder()
                    .text(PlainTextObject.builder().text(bodyMessage).emoji(true).build())
                    .build());

            // 배치 처리 시간 (조건 : Exception이 아닌 경우는 아래 로직 처리하지 않는다.)
            if (StringUtils.hasText(durationMessage)) {
                List<ContextBlockElement> elements = new ArrayList<>();
                elements.add(PlainTextObject.builder().text(durationMessage).build());
                blocks.add(ContextBlock.builder().elements(elements).build());
            }

            // Divide Line
            blocks.add(DividerBlock.builder().build());

            // Payload 설정
            Payload payload = Payload.builder().blocks(blocks).build();
            String m = payload.toString();

            Slack slack = Slack.getInstance();
            slack.send(url, payload);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
