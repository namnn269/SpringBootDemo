package com.example.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3:00"));
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }

}
