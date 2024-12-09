package com.gilbert.spring_boot_batch_service.batch.single;

import com.gilbert.spring_boot_batch_service.dto.BatchData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
@Component
public class SingleStep implements ItemReader<List<BatchData>>, ItemProcessor<List<BatchData>, List<BatchData>>, ItemWriter<List<BatchData>> {
    @Override
    public List<BatchData> process(List<BatchData> item) throws Exception {
        return List.of();
    }

    @Override
    public List<BatchData> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return List.of();
    }

    @Override
    public void write(Chunk<? extends List<BatchData>> chunk) throws Exception {

    }
}
