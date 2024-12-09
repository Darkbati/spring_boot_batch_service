package com.gilbert.spring_boot_batch_service.core.multipleExecutor;

import com.gilbert.spring_boot_batch_service.dto.JobExecuter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MultipleExecutionFactory {
    private final Map<String, MultipleJobExecutor> multipleJobExecutorMap = new ConcurrentHashMap<String, MultipleJobExecutor>();

    private TaskExecutor taskExecutor;

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        if (this.taskExecutor == null) {
            this.taskExecutor = taskExecutor;
        }
    }

    public void add(String jobName, MultipleJobExecutor multipleJobExecutor) {
        multipleJobExecutorMap.put(jobName, multipleJobExecutor);
    }

    public void remove(String jobName) {
        if (multipleJobExecutorMap.containsKey(jobName)) {
            multipleJobExecutorMap.remove(jobName);
        }
    }

    public boolean contains(String jobName) {
        return multipleJobExecutorMap.containsKey(jobName);
    }

    public boolean execution(JobExecuter jobExecuter) {
        if (!multipleJobExecutorMap.containsKey(jobExecuter.getName())) {
            // 등록되지 않는 Job
        }

        if (taskExecutor != null) {
            taskExecutor.execute(() -> {
                multipleJobExecutorMap.get(jobExecuter.getName())
                        .run(jobExecuter);
            });
        } else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    multipleJobExecutorMap.get(jobExecuter.getName())
                            .run(jobExecuter);
                }
            };

            Thread thread = new Thread();
            thread.start();
        }

        return true;
    }
}
