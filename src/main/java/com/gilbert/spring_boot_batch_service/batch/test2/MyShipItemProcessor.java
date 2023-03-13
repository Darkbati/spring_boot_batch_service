package com.gilbert.spring_boot_batch_service.batch.test2;

import com.gilbert.spring_boot_batch_service.dto.BatchData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
@RequiredArgsConstructor
public class MyShipItemProcessor implements ItemProcessor<BatchData, BatchData> {
    @Override
    public BatchData process(BatchData item) throws Exception {
        log.info("###########################################################################");
        log.info("=====> ItemProcessor ");
        log.info("###########################################################################");
        return null;
    }
}
