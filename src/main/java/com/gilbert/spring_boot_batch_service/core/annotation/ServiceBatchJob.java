package com.gilbert.spring_boot_batch_service.core.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceBatchJob {
    String name() default "UNKNOWN_BATCH";
    String description() default "";
    boolean use() default false;
}
