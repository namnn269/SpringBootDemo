package com.example.springbootdemo.config.quartz;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SampleJobService {

    public void exec() {
        System.out.println("Called by Quartz job: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("===================");
    }
}
