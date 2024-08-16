package com.example.springbootdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSettingReq {

    @JsonFormat(pattern = "yyyy-MM-dd'P'HH:mm:ssz")
    private ZonedDateTime zonedDateTime;

    private List<JobType> jobTypes;

    private long duration;

    public enum JobType {
        QUEUE, SCHEDULED, SCHEDULED_RECURRENTLY
    }
}
