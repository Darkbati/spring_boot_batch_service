package com.gilbert.spring_boot_batch_service.common;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

public class JobParameterUtil {
    public static JobParameters convertRawToJobParams(Map<String, Object> properties) {
        return new JobParameters(convertRawToParamMap(properties));
    }

    public static Map<String, JobParameter<?>> convertRawToParamMap(Map<String, Object> properties) {
        return Optional.ofNullable(properties).orElse(emptyMap()).entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> createJobParameter(e.getValue())));
    }

    public static JobParameter<?> createJobParameter(Object value) {
        if (value instanceof Date)
            return new JobParameter((Date) value, Date.class);
        else if (value instanceof Long)
            return new JobParameter((Long) value, Long.class);
        else if (value instanceof Double)
            return new JobParameter((Double) value, Double.class);
        else
            return new JobParameter("" + value, String.class);
    }
}
