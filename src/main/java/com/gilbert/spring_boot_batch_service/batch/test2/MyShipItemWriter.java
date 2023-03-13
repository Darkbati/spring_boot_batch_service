package com.gilbert.spring_boot_batch_service.batch.test2;

import com.gilbert.spring_boot_batch_service.dto.BatchData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class MyShipItemWriter implements ItemWriter<BatchData> {
    @Override
    public void write(Chunk<? extends BatchData> chunk) throws Exception {
        log.info("###########################################################################");
        log.info("=====> ItemWriter ");
        log.info("###########################################################################");
    }
}
