package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.JobSettingReq;
import com.example.springbootdemo.service.JobRunrService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

//@RestController
@RequestMapping("/job-setting")
@RequiredArgsConstructor
public class JobRunrController {

    private final JobScheduler jobScheduler;
    private final JobRunrService jobRunrService;

    @PostMapping
    public void settingJob(@RequestBody JobSettingReq jobSettingReq) {
        for (JobSettingReq.JobType jobType : jobSettingReq.getJobTypes()) {
            switch (jobType) {
                case QUEUE -> jobScheduler.enqueue(UUID.randomUUID(), jobRunrService::doEnqueueJob);
                case SCHEDULED ->
                        jobScheduler.schedule(UUID.randomUUID(), jobSettingReq.getZonedDateTime(), () -> jobRunrService.doScheduledJob(jobSettingReq));
                case SCHEDULED_RECURRENTLY ->
                        jobScheduler.scheduleRecurrently(UUID.randomUUID().toString(), Duration.ofSeconds(jobSettingReq.getDuration()), () -> jobRunrService.doScheduledRecurrentlyJob(jobSettingReq));
            }
        }
    }

    @DeleteMapping
    public void deleteJob(@RequestParam String uuid, @RequestParam boolean isRecurrentJob) {
        if (isRecurrentJob)
            jobScheduler.deleteRecurringJob(uuid);
        else
            jobScheduler.delete(UUID.fromString(uuid));
    }
}
