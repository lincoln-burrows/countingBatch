package com.msk.batch;

import com.msk.batch.job.AggregateTasklet;
import com.msk.batch.model.FinData;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchJobConfig {

    @Bean
    public Job jpaSingleInsertJob(JobRepository jobRepository, Step jpaSingleInsertStep, Step aggregateStep,
                                  PerformanceLoggingListener performanceLoggingListener) {
        return new JobBuilder("jpaSingleInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(performanceLoggingListener)
                .start(jpaSingleInsertStep)
                .next(aggregateStep)
                .build();
    }

    @Bean
    public Job jpaBulkInsertSingleThreadJob(JobRepository jobRepository, Step jpaBulkInsertSingleThreadStep, Step aggregateStep,
                                            PerformanceLoggingListener performanceLoggingListener) {
        return new JobBuilder("jpaBulkInsertSingleThreadJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(performanceLoggingListener)
                .start(jpaBulkInsertSingleThreadStep)
                .next(aggregateStep)
                .build();
    }

    @Bean
    public Job jdbcBulkInsertSingleThreadJob(JobRepository jobRepository, Step jdbcBulkInsertSingleThreadStep, Step aggregateStep,
                                             PerformanceLoggingListener performanceLoggingListener) {
        return new JobBuilder("jdbcBulkInsertSingleThreadJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(performanceLoggingListener)
                .start(jdbcBulkInsertSingleThreadStep)
                .next(aggregateStep)
                .build();
    }

    @Bean
    public Job jpaBulkPartitionInsertJob(JobRepository jobRepository, Step jpaBulkInsertPartitionStep, Step aggregateStep,
                                         PerformanceLoggingListener performanceLoggingListener) {
        return new JobBuilder("jpaBulkPartitionInsertJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(performanceLoggingListener)
                .start(jpaBulkInsertPartitionStep)
                .next(aggregateStep)
                .build();
    }


    @Bean
    public Step jpaSingleInsertStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            MultiResourceItemReader<FinData> multiResourceItemReader, ItemWriter<FinData> finDataJpaSingleWriter,
                            PerformanceLoggingListener performanceLoggingListener) {
        return new StepBuilder("jpaSingleInsertStep", jobRepository)
                .<FinData, FinData>chunk(1000)
                .reader(multiResourceItemReader)
                .writer(finDataJpaSingleWriter)
                .listener(performanceLoggingListener)
                .transactionManager(transactionManager)
                .build();
    }


    @Bean
    public Step jpaBulkInsertSingleThreadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            MultiResourceItemReader<FinData> multiResourceItemReader, JpaItemWriter<FinData> finDataJpaWriter,
                            PerformanceLoggingListener performanceLoggingListener) {
        return new StepBuilder("jpaBulkInsertSingleThreadStep", jobRepository)
                .<FinData, FinData>chunk(100_000)
                .reader(multiResourceItemReader)
                .writer(finDataJpaWriter)
                .listener(performanceLoggingListener)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Step jdbcBulkInsertSingleThreadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            MultiResourceItemReader<FinData> multiResourceItemReader, ItemWriter<FinData> finDataJdbcBulkWriter,
                            PerformanceLoggingListener performanceLoggingListener) {
        return new StepBuilder("jdbcBulkInsertSingleThreadStep", jobRepository)
                .<FinData, FinData>chunk(100_000)
                .reader(multiResourceItemReader)
                .writer(finDataJdbcBulkWriter)
                .listener(performanceLoggingListener)
                .transactionManager(transactionManager)
                .build();
    }


    @Bean
    public Step aggregateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              AggregateTasklet aggregateTasklet,
                              PerformanceLoggingListener performanceLoggingListener) {
        return new StepBuilder("aggregateStep", jobRepository)
                .tasklet(aggregateTasklet, transactionManager)
                .listener(performanceLoggingListener)
                .build();
    }
}
