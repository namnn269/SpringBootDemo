package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.DateForm;
import com.example.springbootdemo.dto.MyTime;
import com.example.springbootdemo.dto.ObjectDto;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/time")
public class TimeController {

    @GetMapping("/instant")
    public ResponseEntity<?> instant(@RequestBody MyTime time) {
        System.out.println(time);
        return ResponseEntity.ok().body(time);
    }

    @PostMapping(value = "/date-body")
    public Object dateFormatBody(@RequestBody DateForm dateForm) {
        System.out.println("in post: " + dateForm);
        return ResponseEntity.ok(dateForm);
    }

    @GetMapping(value = "/date-format")
    public Object dateFormatParam(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date date) {
        System.out.println("date-format: " + date);
        return new ObjectDto(date);
    }

    @GetMapping(value = "/date-with-offset")
    public Object dateWithOffset(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX") Date date) {
        System.out.println("date-with-offset: " + date);
        return new ObjectDto(date);
    }

    @GetMapping("/local-date-time")
    public Object localDateTime(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime localDateTime) {
        System.out.println("local-date-time: " + localDateTime);
        return new ObjectDto(localDateTime);
    }

    @GetMapping("/zone-date-time")
    public Object zoneDateTime(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX") ZonedDateTime zonedDateTime) {
        System.out.println("zone-date-time: " + zonedDateTime);
        return new ObjectDto(zonedDateTime);
    }


    @GetMapping("/zone-date-time-2")
    public Object zoneDateTime2(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss'['z']'") ZonedDateTime zonedDateTime) {
        System.out.println("zone-date-time-2: " + zonedDateTime);
        return new ObjectDto(zonedDateTime);
    }

    @GetMapping("/zone-date-time-3")
    public Object zoneDateTime3(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss'*'VV'*'") ZonedDateTime zonedDateTime) {
        System.out.println("zone-date-time-3: " + zonedDateTime);
        return new ObjectDto(zonedDateTime);
    }

    @GetMapping("/offset-date-time")
    public Object offsetDateTime(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX") OffsetDateTime offsetDateTime) {
        System.out.println("offset-date-time: " + offsetDateTime);
        return new ObjectDto(offsetDateTime);
    }

    @GetMapping("/instant-date")
    public Object instant1(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX") Instant instant) {
        System.out.println("instant-date: " + instant);
        return new ObjectDto(instant);
    }

    @GetMapping("/instant-number")
    public Object instant2(@RequestParam long instant) {
        System.out.println("instant-number: " + instant);
        return new ObjectDto(instant);
    }

    @GetMapping("/old-date")
    public Object oldDate() {
        Date date = new Date();
        Date date1 = DateUtils.ceiling(new Date(), Calendar.SECOND);
        Date date2 = DateUtils.ceiling(new Date(), Calendar.MINUTE);
        Date date3 = DateUtils.ceiling(new Date(), Calendar.HOUR);
        System.out.println(date);
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        Pair<String, Integer> pair = new MutablePair<>();
        pair.setValue(56);
        pair.of("","");
        return List.of(date, date1, date2, date3);
    }
}













