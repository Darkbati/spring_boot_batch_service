package com.gilbert.spring_boot_batch_service.core.multipleExecutor;

import com.gilbert.spring_boot_batch_service.dto.JobExecuter;

public interface MultipleJobExecutor {
    void run(JobExecuter jobExecuter);
}
