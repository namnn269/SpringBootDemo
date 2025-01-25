package com.example.springbootdemo.controller;

import com.example.springbootdemo.config.quartz.SampleJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.quartz.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

@RestController
@RequestMapping("/quartz-job")
@AllArgsConstructor
public class QuartzJobRegisterController {

    private final Scheduler scheduler;

    @PostMapping("/register")
    public Object registerJob(@RequestBody CreateJobRequest request) throws SchedulerException {

        JobKey jobKey = new JobKey(request.getJobName(), request.getJobGroup());

        if (scheduler.checkExists(jobKey)) {
            return ResponseEntity.status(400).body("job already exists");
        }

        JobDetail jobDetail = JobBuilder.newJob()
                .withIdentity(jobKey)
                .ofType(SampleJob.class)
                .storeDurably(true)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder
                        .cronSchedule(request.getCronExpression())
                        .inTimeZone(TimeZone.getTimeZone("+07:00")))
                .endAt(null)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        return ResponseEntity.ok().body("created a job success");
    }

    @DeleteMapping(path = "{groupName}/{jobName}")
    public Object deleteJob(@PathVariable String groupName,
                            @PathVariable String jobName) throws SchedulerException {
        boolean deleted = scheduler.deleteJob(new JobKey(jobName, groupName));
        return ResponseEntity.ok().body(deleted ? "deleted a job success" : "failed to delete a job");
    }

    @Getter
    @Setter
    public static class CreateJobRequest {
        private String jobName;
        private String jobGroup;
        private String cronExpression;
    }

}
