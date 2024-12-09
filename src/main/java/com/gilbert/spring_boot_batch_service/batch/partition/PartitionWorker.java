package com.gilbert.spring_boot_batch_service.batch.partition;

import com.gilbert.spring_boot_batch_service.dto.BatchData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@StepScope
@Component
public class PartitionWorker implements ItemStream, ItemReader<List<BatchData>>, ItemWriter<List<BatchData>> {
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        ItemStream.super.close();
    }

    @Override
    public void write(Chunk<? extends List<BatchData>> chunk) throws Exception {

    }

    @Override
    public List<BatchData> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return List.of();
    }
}
