package com.msk.batch.partition;

import com.msk.batch.model.FinData;
import com.msk.batch.reader.FinDataPartitionReader;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PartitionJobConfig {

    @Bean
    @Qualifier
    public Step partitionStep(
            JobRepository jobRepository,
            FilePartitioner filePartitioner,
            PartitionHandler partitionHandler) {

        return new StepBuilder("partitionStep", jobRepository)
                .partitioner("workerStep", filePartitioner)
                .partitionHandler(partitionHandler)
                .build();
    }

    @Bean
    public PartitionHandler partitionHandler(
            @Qualifier("workerStep") Step workerStep,
            TaskExecutor partitionTaskExecutor) {

        TaskExecutorPartitionHandler handler =
                new TaskExecutorPartitionHandler();

        handler.setStep(workerStep);
        handler.setTaskExecutor(partitionTaskExecutor);
        handler.setGridSize(5);

        return handler;
    }

    @Bean
    public TaskExecutor partitionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("partition-");
        executor.initialize();

        return executor;
    }

    //jpa select 문 방지 -> 원리 파악
    @Bean
    @Qualifier
    public Step workerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            @Qualifier("partitionFinDataItemReader") FlatFileItemReader<FinData> itemReader, JpaItemWriter<FinData> finDataJpaWriter) {
        return new StepBuilder("processStep", jobRepository)
                .<FinData, FinData>chunk(100_000)
                .reader(itemReader)
                .writer(finDataJpaWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
