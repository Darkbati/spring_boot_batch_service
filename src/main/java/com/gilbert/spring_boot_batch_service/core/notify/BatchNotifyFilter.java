package com.gilbert.spring_boot_batch_service.core.notify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.gilbert.spring_boot_batch_service.config.BatchDataConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class BatchNotifyFilter extends AbstractMatcherFilter<ILoggingEvent> {
    private final String TARGET_CLASS = "org.springframework.batch.core.step.AbstractStep";
    protected Level level;

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchDataConfig.class);
    private Map<String, String> expiringMap = (Map<String, String>) ctx.getBean("expiringMap");

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getLevel().levelInt == Level.ERROR.levelInt) {
            if (event.getLoggerName().contains(TARGET_CLASS)) {
                // Slack Notify 대상을 찾아 Slack Message Send 처리

                System.out.println("");
            }
        }

        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        } else {
            return event.getLevel().equals(this.level) ? this.onMatch : this.onMismatch;
        }
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}
