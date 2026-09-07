package com.msk.batch;

import com.msk.batch.job.AggregateTasklet;
import com.msk.batch.model.FinData;
import com.msk.batch.model.FinData;
import com.msk.batch.reader.TimedFinDataReader;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchJobConfig {

    @Bean
    public Job getAnalysis(JobRepository jobRepository, Step processStep, Step aggregateStep) {
        return new JobBuilder("finToEvent", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processStep)
                .next(aggregateStep)
                .build();
    }
// 단건 insert 방식
//    @Bean
//    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
//                            MultiResourceItemReader<FinData> multiResourceItemReader, JdbcBatchItemWriter<FinData> finDataWriter) {
//        return new StepBuilder("processStep", jobRepository)
//                .<FinData, FinData>chunk(1000)
//                .reader(multiResourceItemReader)
//                .writer(finDataWriter)
//                .transactionManager(transactionManager)
//                .build();
//    }

    //jpa save all
//    @Bean
//    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
//                            MultiResourceItemReader<FinData> multiResourceItemReader, ItemWriter<FinData> finDataJpaBulkWriter) {
//        return new StepBuilder("processStep", jobRepository)
//                .<FinData, FinData>chunk(100_000)
//                .reader(multiResourceItemReader)
//                .writer(finDataJpaBulkWriter)
//                .transactionManager(transactionManager)
//                .build();
//    }

    //jpa select 문 방지 -> 원리 파악
    @Bean
    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            MultiResourceItemReader<FinData> multiResourceItemReader, JpaItemWriter<FinData> finDataJpaWriter) {
        return new StepBuilder("processStep", jobRepository)
                .<FinData, FinData>chunk(100_000)
                .reader(multiResourceItemReader)
                .writer(finDataJpaWriter)
                .transactionManager(transactionManager)
                .build();
    }

    // jdbc bulk insert
//    @Bean
//    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
//                            MultiResourceItemReader<FinData> multiResourceItemReader, ItemWriter<FinData> finDataBulkWriter) {
//        return new StepBuilder("processStep", jobRepository)
//                .<FinData, FinData>chunk(100_000)
//                .reader(multiResourceItemReader)
//                .writer(finDataBulkWriter)
//                .transactionManager(transactionManager)
//                .build();
//    }

// 파일 read 시간 측정용
//    @Bean
//    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
//                            TimedFinDataReader timedFinDataReader, ItemWriter<FinData> finDataBulkWriter) {
//        return new StepBuilder("processStep", jobRepository)
//                .<FinData, FinData>chunk(100_000)
//                .reader(timedFinDataReader)
//                .writer(finDataBulkWriter)
//                .transactionManager(transactionManager)
//                .build();
//    }


    @Bean
    public Step aggregateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              AggregateTasklet aggregateTasklet) {
        return new StepBuilder("aggregateStep", jobRepository)
                .tasklet(aggregateTasklet, transactionManager)
                .build();
    }
}
