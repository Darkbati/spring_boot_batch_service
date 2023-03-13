package com.gilbert.spring_boot_batch_service.batch.test2;

import com.gilbert.spring_boot_batch_service.dto.BatchData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
@RequiredArgsConstructor
public class MyShipItemReader implements ItemReader<BatchData> {
    @Override
    public BatchData read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("###########################################################################");
        log.info("=====> ItemReader ");
        log.info("###########################################################################");

        // null 로 리턴하게 되면 Paging 처리가 중단된다.
        return null;
    }
}
