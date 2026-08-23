package com.msk.batch;

import com.msk.batch.job.AggregateTasklet;
import com.msk.batch.model.FinData;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchJobConfig {

    @Bean
    public Job getAnalysis(JobRepository jobRepository, Step processStep, Step aggregateStep) {
        return new JobBuilder("finToEvent", jobRepository)
                .start(processStep)
                .next(aggregateStep)
                .build();
    }

    @Bean
    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            MultiResourceItemReader multiResourceItemReader, JdbcBatchItemWriter<FinData> finDataWriter) {
        return new StepBuilder("processStep", jobRepository)
                .<FinData, FinData>chunk(1000, transactionManager)
                .reader(multiResourceItemReader)
                .writer(finDataWriter)
                .build();
    }

    @Bean
    public Step aggregateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              AggregateTasklet aggregateTasklet) {
        return new StepBuilder("aggregateStep", jobRepository)
                .tasklet(aggregateTasklet, transactionManager)
                .build();
    }
}
