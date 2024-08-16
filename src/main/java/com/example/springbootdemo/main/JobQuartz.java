package com.example.springbootdemo.main;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class JobQuartz {
    public static void main(String[] args) throws SchedulerException, InterruptedException {

        Scheduler scheduler = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JobKey jobKey = new JobKey("myJob", "groupJob1");
            JobDetail jobDetail = org.quartz.JobBuilder
                    .newJob(SampleJob.class)
                    .usingJobData("key1", "value1")
                    .withIdentity(jobKey)
                    .storeDurably(true)
                    .build();

            TriggerKey triggerKey = new TriggerKey("myTrigger", "groupTrigger1");

            SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder
                    .repeatSecondlyForTotalCount(4)
                    .withIntervalInSeconds(5)
                    .withMisfireHandlingInstructionNowWithExistingCount();

            CronScheduleBuilder cronBuilder = CronScheduleBuilder
                    .cronSchedule("02/10 48 22 ? 8 7")
                    .inTimeZone(TimeZone.getTimeZone(ZoneId.of("+07:00")));

            Trigger myTrigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(triggerKey)
                    .startAt(format.parse("2024-08-03 22:48:01"))
                    .endAt(format.parse("2024-08-03 22:48:44"))
                    .withSchedule(cronBuilder)
                    .build();

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();

            scheduler.addJob(jobDetail, true);
            scheduler.getListenerManager()
                    .addJobListener(new MyJobListener(), key -> key == jobKey);
            scheduler.scheduleJob(myTrigger);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (scheduler != null)
                scheduler.shutdown();
        }

    }

    public static class SampleJob implements org.quartz.Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            Object value = context.getMergedJobDataMap().get("key1");
            System.out.println(value +
                    " ==> " + Thread.currentThread().getName() +
                    " ==> " + LocalDateTime.now());
        }
    }

    public static class MyJobListener implements JobListener {

        @Override
        public String getName() {
            return "my_job_listener";
        }

        @Override
        public void jobToBeExecuted(JobExecutionContext context) {
            System.out.println("START LISTENER");
        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext context) {
            System.out.println("DENIED LISTENER");
        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
            System.out.println("END LISTENER");
        }
    }
}
