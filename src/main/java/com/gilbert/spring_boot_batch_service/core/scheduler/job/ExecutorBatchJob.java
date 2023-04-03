package com.gilbert.spring_boot_batch_service.core.scheduler.job;

import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
        Map<String, Object> map = context.getJobDetail().getJobDataMap().getWrappedMap();
        String batchName = String.valueOf(map.get("batchName"));
        String dateFormat = String.valueOf(map.get("dateFormat"));
        String dateFunction = String.valueOf(map.get("dateFunction"));

        Map<String, Object> param = new HashMap<>();
        param.put("job.name", batchName);

        if (StringUtils.hasText(dateFunction)) {
            param.put("startDate", getLocalDateTime(dateFormat, dateFunction));
        }

        try {
            // version 값이 동일하면 중복 실행이 안된다.
            List<JobExecution> list = batchExecutionService.getExecutionList(batchName);
            if (list != null && list.size() > 0) {
                param.put("version", list.get(0).getJobId());
            } else {
                param.put("version", 0);
            }

            batchExecutionService.launch(JobExecuter.builder().name(batchName).parameter(param).build());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /*
    // 하기 함수 유형에 대해 처리할 수 있도록 정의된 함수
    LocalDate.now();
    LocalDate.now().plusDays(1);
    LocalDate.now().plusDays(1).plusYears(2);
    LocalDate.now().minusDays(5).minusMonths(3).minusYears(2);
    LocalDateTime.now()
    LocalDateTime.now().minusHours(5).plusMinutes(10).minusSeconds(20);
    LocalDateTime.now().minusDays(5).plusYears(5).plusSeconds(50);
     */
    private String getLocalDateTime(String dateFormat, String dateFunction) {
        StringTokenizer token = new StringTokenizer(dateFunction, ".");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime localDateTime = LocalDateTime.now();

        while (token.hasMoreTokens()) {
            String function = token.nextToken();

            if (function.contains("plusDays")) {
                String value = function.substring("plusDays".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusDays(Integer.valueOf(value));
                }
            } else if (function.contains("plusMonths")) {
                String value = function.substring("plusMonths".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusMonths(Integer.valueOf(value));
                }
            } else if (function.contains("plusWeeks")) {
                String value = function.substring("plusWeeks".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusWeeks(Integer.valueOf(value));
                }
            } else if (function.contains("plusYears")) {
                String value = function.substring("plusYears".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusYears(Integer.valueOf(value));
                }
            } else if (function.contains("minusDays")) {
                String value = function.substring("minusDays".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusDays(Integer.valueOf(value));
                }
            } else if (function.contains("minusMonths")) {
                String value = function.substring("minusMonths".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusMonths(Integer.valueOf(value));
                }
            } else if (function.contains("minusWeeks")) {
                String value = function.substring("minusWeeks".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusWeeks(Integer.valueOf(value));
                }
            } else if (function.contains("minusYears")) {
                String value = function.substring("minusYears".length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusYears(Integer.valueOf(value));
                }
            }
        }

        return formatter.format(localDateTime);
    }
}
