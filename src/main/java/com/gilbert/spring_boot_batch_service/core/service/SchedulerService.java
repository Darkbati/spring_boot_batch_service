package com.gilbert.spring_boot_batch_service.core.service;

import com.gilbert.spring_boot_batch_service.core.advise.code.ErrorCode;
import com.gilbert.spring_boot_batch_service.core.advise.exception.SchedulerException;
import com.gilbert.spring_boot_batch_service.core.scheduler.job.RunJob;
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

    public void addScheduler(RequestSchedulerJob requestSchedulerJob) throws Exception {
        JobKey jobKey = new JobKey(requestSchedulerJob.getName(), JOB_GROUP);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }

        createJob(jobKey, requestSchedulerJob);
    }

    public void removeScheduler(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(ErrorCode.NOT_FOUND_SCHEDULER);
        }

        scheduler.deleteJob(jobKey);
    }

    public void modifyScheduler(RequestSchedulerJob requestSchedulerJob) throws Exception {
        JobKey jobKey = new JobKey(requestSchedulerJob.getName(), JOB_GROUP);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(ErrorCode.NOT_FOUND_SCHEDULER);
        }

        scheduler.deleteJob(jobKey);
        createJob(jobKey, requestSchedulerJob);
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
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!scheduler.checkExists(jobKey)) {
            throw new SchedulerException(ErrorCode.NOT_FOUND_SCHEDULER);
        }

        return getJobDetail(jobKey);
    }

    private SchedulerDetail getJobDetail(JobKey jobKey) throws Exception {
        SchedulerDetail schedulerDetail = new SchedulerDetail();
        schedulerDetail.setJobName(jobKey.getName());
        schedulerDetail.setJobGroup(jobKey.getGroup());

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        schedulerDetail.setDescription(jobDetail.getDescription());
        schedulerDetail.setJobDataMap(jobDetail.getJobDataMap().getWrappedMap());

        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        if (triggers != null && triggers.size() > 0) {
            schedulerDetail.setNextFireTime(triggers.get(0).getNextFireTime());
            schedulerDetail.setPreviousFireTime(triggers.get(0).getPreviousFireTime());
            schedulerDetail.setPriority(triggers.get(0).getPriority());
        }

        return schedulerDetail;
    }

    private void createJob(JobKey jobKey, RequestSchedulerJob requestSchedulerJob) throws Exception {
        JobDataMap jobDataMap = new JobDataMap();
        if (requestSchedulerJob.getParam() != null) {
            jobDataMap.putAll(requestSchedulerJob.getParam());
        }

        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(jobKey)
                .withDescription(requestSchedulerJob.getDescription())
                .setJobData(jobDataMap)
                .ofType(RunJob.class)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(requestSchedulerJob.getCronSchedule())).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
