package com.gilbert.spring_boot_batch_service;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringBootBatchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootBatchServiceApplication.class, args);
    }

}
