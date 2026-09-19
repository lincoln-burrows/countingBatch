package com.msk.batch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@Component
public class JobRunner implements CommandLineRunner {

    private final JobOperator jobOperator;
    private final Map<String, Job> jobs;

    public JobRunner(JobOperator jobOperator, Map<String, Job> jobs) {
        this.jobs = jobs;
        this.jobOperator = jobOperator;
    }

    @Override
    public void run(String... args) throws Exception {
        String jobName = findJobName(args);
        Job job = jobs.get(jobName);

        if (job == null) {
            throw new IllegalArgumentException("Unknown jobName=" + jobName + ", availableJobs=" + jobs.keySet());
        }

        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addLocalDate("date", LocalDate.of(2026, 1, 1))
                // .addString(null, null)
                // .addDouble(null, null)
                // .addLocalDate(null, null)
                .toJobParameters();

        jobOperator.start(job, jobParameter);
    }

    private String findJobName(String[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith("--job="))
                .map(arg -> arg.substring("--job=".length()))
                .findFirst()
                .orElse("jpaBulkPartitionInsertJob");
    }
}
