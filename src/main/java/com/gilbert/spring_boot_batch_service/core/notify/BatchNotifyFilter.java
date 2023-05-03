package com.gilbert.spring_boot_batch_service.core.notify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.gilbert.spring_boot_batch_service.config.BatchDataConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Map;

public class BatchNotifyFilter extends AbstractMatcherFilter<ILoggingEvent> {
    protected Level level;
    private final String TARGET_CLASS = "org.springframework.batch.core.step.AbstractStep";
    private final String IN_JOB = "in job";

    private ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchDataConfig.class);
    private Map<String, String> expiringMap = (Map<String, String>) ctx.getBean("expiringMap");

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getLevel().levelInt == Level.ERROR.levelInt) {
            if (event.getLoggerName().contains(TARGET_CLASS)) {
                String message = event.getMessage();
                if (message.contains(IN_JOB) && event.hasCallerData()) {
                    String batchName = message.substring(message.indexOf(IN_JOB) + IN_JOB.length(), message.length()).trim();

                    int i = event.getCallerData().length;

                    IThrowableProxy throwableProxy = event.getThrowableProxy();
                    String errorMessage = throwableProxy.getMessage();

                    Arrays.stream(event.getCallerData()).anyMatch(e -> {
                        String fileName = e.getFileName();
                        Integer line = e.getLineNumber();
                        String classStr = e.getClassName();

                        return false;
                    });
                }
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
