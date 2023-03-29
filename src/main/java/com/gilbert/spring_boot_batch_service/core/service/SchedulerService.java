package com.gilbert.spring_boot_batch_service.core.service;

import com.gilbert.spring_boot_batch_service.core.advice.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advice.exception.SchedulerCustomException;
import com.gilbert.spring_boot_batch_service.core.scheduler.job.ExecutorBatchJob;
import com.gilbert.spring_boot_batch_service.dto.SchedulerDetail;
import com.gilbert.spring_boot_batch_service.dto.request.RequestSchedulerJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchedulerService {
    public static final String JOB_GROUP = "IFLAND_JOB_GROUP";

    private final Scheduler scheduler;

    public SchedulerDetail addScheduler(RequestSchedulerJob requestSchedulerJob) throws Exception {
        JobKey jobKey = new JobKey(requestSchedulerJob.getName(), JOB_GROUP);
        if (scheduler.checkExists(jobKey)) {
            throw new SchedulerCustomException(ErrorCode.ALREADY_REGISTRY_JOB);
        }

        return this.createJob(jobKey, requestSchedulerJob);
    }

    public void removeScheduler(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerCustomException(ErrorCode.NOT_FOUND_SCHEDULER);
        }

        scheduler.deleteJob(jobKey);
    }

    public SchedulerDetail modifyScheduler(RequestSchedulerJob requestSchedulerJob) throws Exception {
        JobKey jobKey = new JobKey(requestSchedulerJob.getName(), JOB_GROUP);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerCustomException(ErrorCode.NOT_FOUND_SCHEDULER);
        }

        scheduler.deleteJob(jobKey);
        return this.createJob(jobKey, requestSchedulerJob);
    }

    public List<SchedulerDetail> schedulerList() throws Exception {
        List<SchedulerDetail> list = new ArrayList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                list.add(getJobDetail(jobKey));
            }
        }

        return list;
    }

    public SchedulerDetail schedulerDetail(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = this.checkJobKey(jobName, jobGroup);
        return this.getJobDetail(jobKey);
    }

    public void schedulerPause(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = this.checkJobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    public void schedulerResume(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = this.checkJobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    private JobKey checkJobKey(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerCustomException(ErrorCode.NOT_FOUND_SCHEDULER);
        }

        return jobKey;
    }

    private SchedulerDetail getJobDetail(JobKey jobKey) throws Exception {
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        if (triggers != null && triggers.size() > 0) {
            Trigger trigger = triggers.get(0);

            return SchedulerDetail.builder()
                    .jobName(jobKey.getName())
                    .jobGroup(jobKey.getGroup())
                    .description(jobDetail.getDescription())
                    .jobDataMap(jobDetail.getJobDataMap().getWrappedMap())
                    .nextFireTime(trigger.getNextFireTime())
                    .previousFireTime(trigger.getPreviousFireTime())
                    .priority(trigger.getPriority())
                    .triggerStatus(scheduler.getTriggerState(trigger.getKey()).name())
                    .build();
        }

        return SchedulerDetail.builder()
                .jobName(jobKey.getName())
                .jobGroup(jobKey.getGroup())
                .description(jobDetail.getDescription())
                .jobDataMap(null)
                .nextFireTime(null)
                .previousFireTime(null)
                .priority(null)
                .triggerStatus(null)
                .build();
    }

    private SchedulerDetail createJob(JobKey jobKey, RequestSchedulerJob requestSchedulerJob) throws Exception {
        JobDataMap jobDataMap = new JobDataMap();
        if (requestSchedulerJob.getParam() != null) {
            jobDataMap.putAll(requestSchedulerJob.getParam());
        }

        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(jobKey)
                .withDescription(requestSchedulerJob.getDescription())
                .setJobData(jobDataMap)
                .ofType(ExecutorBatchJob.class)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(requestSchedulerJob.getCronSchedule())).build();
        scheduler.scheduleJob(jobDetail, trigger);

        return SchedulerDetail.builder()
                .jobName(jobKey.getName())
                .jobGroup(jobKey.getGroup())
                .description(jobDetail.getDescription())
                .jobDataMap(jobDataMap.getWrappedMap())
                .nextFireTime(trigger.getNextFireTime())
                .previousFireTime(trigger.getPreviousFireTime())
                .priority(trigger.getPriority())
                .triggerStatus(scheduler.getTriggerState(trigger.getKey()).name())
                .build();
    }
}
