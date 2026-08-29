package com.msk.batch.job;

import com.msk.batch.model.FinData;
import com.msk.batch.model.Track;
import com.msk.batch.service.AggregateService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AggregateTasklet implements Tasklet {

    private final AggregateService aggregateService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        aggregateService.aggregate();

        return RepeatStatus.FINISHED;
    }



        }
