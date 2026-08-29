package com.msk.batch.job;

import com.msk.batch.service.AggregateService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AggregateTasklet implements Tasklet {

    private final AggregateService aggregateService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        aggregateService.aggregate();

        return RepeatStatus.FINISHED;
    }


}
