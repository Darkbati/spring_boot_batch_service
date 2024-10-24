package com.gilbert.spring_boot_batch_service.core.scheduler.job;

import com.gilbert.spring_boot_batch_service.core.service.BatchExecutionService;
import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

// @DisallowConcurrentExecution는 QuartzJob이 중복 실행되는 것을 예방하는 어노테이션
@Slf4j
@DisallowConcurrentExecution
public class ExecutorBatchJob extends QuartzJobBean {
    @Resource
    private BatchExecutionService batchExecutionService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Map<String, Object> map = context.getJobDetail().getJobDataMap().getWrappedMap();
        String jobName = context.getJobDetail().getKey().getName();
        String dateName = String.valueOf(map.get("dateName"));
        String dateFormat = String.valueOf(map.get("dateFormat"));
        String dateFunction = String.valueOf(map.get("dateFunction"));

        Map<String, Object> param = new HashMap<>();
        param.put("job.name", jobName);

        if (dateName.toLowerCase().contains("date")) {
            param.put(dateName, getLocalDateTime(dateFormat, dateFunction));
        }

        try {
            param.put("version", batchExecutionService.getJobInstanceId(jobName));

            log.info("Batch Job Name : {} , DateFormat : {}, DateFunction : {}, Version : {}", jobName, dateFormat, dateFunction, param.get("version"));
            batchExecutionService.launch(JobExecuter.builder().name(jobName).parameter(param).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        final String PLUS_DAYS = "plusDays";
        final String PLUS_MONTHS = "plusMonths";
        final String PLUS_WEEKS = "plusWeeks";
        final String PLUS_YEARS = "plusYears";
        final String MINUS_DAYS = "minusDays";
        final String MINUS_MONTHS = "minusMonths";
        final String MINUS_WEEKS = "minusWeeks";
        final String MINUS_YEARS = "minusYears";

        StringTokenizer token = new StringTokenizer(dateFunction, ".");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime localDateTime = LocalDateTime.now();

        while (token.hasMoreTokens()) {
            String function = token.nextToken();

            if (function.contains(PLUS_DAYS)) {
                String value = function.substring(PLUS_DAYS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusDays(Integer.valueOf(value));
                }
            } else if (function.contains(PLUS_MONTHS)) {
                String value = function.substring(PLUS_MONTHS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusMonths(Integer.valueOf(value));
                }
            } else if (function.contains(PLUS_WEEKS)) {
                String value = function.substring(PLUS_WEEKS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusWeeks(Integer.valueOf(value));
                }
            } else if (function.contains(PLUS_YEARS)) {
                String value = function.substring(PLUS_YEARS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.plusYears(Integer.valueOf(value));
                }
            } else if (function.contains(MINUS_DAYS)) {
                String value = function.substring(MINUS_DAYS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusDays(Integer.valueOf(value));
                }
            } else if (function.contains(MINUS_MONTHS)) {
                String value = function.substring(MINUS_MONTHS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusMonths(Integer.valueOf(value));
                }
            } else if (function.contains(MINUS_WEEKS)) {
                String value = function.substring(MINUS_WEEKS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusWeeks(Integer.valueOf(value));
                }
            } else if (function.contains(MINUS_YEARS)) {
                String value = function.substring(MINUS_YEARS.length() + 1, function.length() - 1);
                if (StringUtils.hasText(value)) {
                    localDateTime = localDateTime.minusYears(Integer.valueOf(value));
                }
            }
        }

        return formatter.format(localDateTime);
    }
}
