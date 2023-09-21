package com.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/multi-thread")
public class MultiThreadController {

    @GetMapping
    public Object get() {
        Map<Object, Object> map = new HashMap<>();
        map.put("date", new Date());
        map.put("int", 56);
        map.put("str", "str");

        Thread thread = new Thread(() -> {
            System.out.println("start ==> " + Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("end ==> " + Thread.currentThread().getName());
            System.out.println(map);
        });
        thread.start();
        return map;
    }
}
