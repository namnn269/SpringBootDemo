package com.example.springbootdemo.service;

import com.example.springbootdemo.dto.JobSettingReq;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JobRunrService {
    public void doEnqueueJob() {
        System.out.println("=========");
        System.out.println("enqueue job");
        System.out.println("actual time: " + ZonedDateTime.now(ZoneId.of("UTC")));
    }

    public void doScheduledJob(JobSettingReq jobSettingReq) {
        ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime settingTime = jobSettingReq.getZonedDateTime();
        System.out.println("=========");
        System.out.println("scheduled job");
        System.out.println("setting time: " + settingTime);
        System.out.println("actual time: " + utc);
        System.out.printf("gap: %s s%n", utc.toEpochSecond() - settingTime.toEpochSecond());
    }

    public void doScheduledRecurrentlyJob(JobSettingReq jobSettingReq) {
        long duration = jobSettingReq.getDuration();
        ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
        System.out.println("=========");
        System.out.println("scheduled recurrently job");
        System.out.printf("every %s s%n", duration);
        System.out.println("actual time: " + utc);
    }
}
