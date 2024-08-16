package com.example.springbootdemo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.listener.CompositeJobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
//@Configuration
//@EnableBatchProcessing
@RequiredArgsConstructor
public class JobBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobLauncher jobLauncher;

    @Bean
    public Job firstJob(Step chunkOrientedStep, Step taskletStep, Step taskletStep2, Flow flow1) {
        JobParametersBuilder builder = new JobParametersBuilder()
                .addDate("date", new Date())
                .addString("string", "value", true);
        System.out.println(builder);
        return new JobBuilder("job1", jobRepository)
                .start(chunkOrientedStep)
                .on("FAILED").to(taskletStep)
                .from(chunkOrientedStep).on("*").to(taskletStep)
                .next(taskletStep2)
                .from(taskletStep2).on("FAILED").to(flow1)
                .end()
                .listener(jobExecutionListener())
                .validator(jobParametersValidator())
                .build();
    }

    @Bean
    public Flow flow1(Step chunkOrientedStep, Step taskletStep, Step taskletStep2) {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(chunkOrientedStep)
                .next(jobExecutionDecider()).on("COMPLETED").to(taskletStep)
                .from(chunkOrientedStep).on("FAILED").to(taskletStep2)
                .build();
    }

    @Bean
    public Step chunkOrientedStep() {
        return new StepBuilder("chunkStep", jobRepository)
                .<String, LocalDate>chunk(1, platformTransactionManager)
                .reader(() -> "input")
                .processor(LocalDate::parse)
                .writer(chunk -> {
                    List<? extends LocalDate> items = chunk.getItems();
                    log.info("size: " + items.size());
                })
                .startLimit(1)
                .allowStartIfComplete(true)
                .faultTolerant()
                .skipLimit(5)
                .skip(FlatFileFormatException.class)
                .retryLimit(3)
                .retry(CannotAcquireLockException.class)
                .noRollback(ValidationException.class)
                .readerIsTransactionalQueue()
                .build();
    }

    @Bean
    public Step taskletStep() {
        return new StepBuilder("taskletStep", jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, platformTransactionManager)
                .build();
    }

    @Bean
    public Step taskletStep2() {
        return new StepBuilder("taskletStep", jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, platformTransactionManager)
                .build();
    }

    public JobExecutionListener jobExecutionListener() {
        CompositeJobExecutionListener container = new CompositeJobExecutionListener();
        JobExecutionListener listener = new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                BatchStatus status = jobExecution.getStatus();
                System.out.println(status);
            }
        };
        container.register(listener);
        return container;
    }

    public JobParametersValidator jobParametersValidator() {
        return parameters -> {
            if (parameters != null && parameters.getLocalDate("date") == null) {
                log.warn("date is required");
            }
        };
    }

    public JobExecutionDecider jobExecutionDecider() {
        return (jobExecution, stepExecution) -> new FlowExecutionStatus("COMPLETED");
    }
}
