package com.msk.batch.reader;

import com.msk.batch.model.FinData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimedFinDataReader implements ItemStreamReader<FinData> {

    private final MultiResourceItemReader<FinData> delegate;

    private long totalReadNanos = 0;
    private long totalCount = 0;

    public TimedFinDataReader(
            MultiResourceItemReader<FinData> delegate) {
        this.delegate = delegate;
    }

    @Override
    public FinData read() throws Exception {

        long start = System.nanoTime();

        FinData item = delegate.read();

        long elapsed = System.nanoTime() - start;

        totalReadNanos += elapsed;

        if (item != null) {
            totalCount++;
        }

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) {
        delegate.update(executionContext);
    }

    @Override
    public void close() {
        delegate.close();

        log.info(
                "READ 총 {}건, 총 소요시간={}초",
                totalCount,
                totalReadNanos / 1_000_000_000.0
        );
    }
}