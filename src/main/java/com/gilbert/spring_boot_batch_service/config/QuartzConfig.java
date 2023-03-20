package com.gilbert.spring_boot_batch_service.config;

import com.gilbert.spring_boot_batch_service.core.scheduler.AutowiringSpringBeanJobFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class QuartzConfig {
    private final ApplicationContext applicationContext;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setTaskExecutor(threadPoolTaskExecutor);
        return schedulerFactoryBean;
    }
}
