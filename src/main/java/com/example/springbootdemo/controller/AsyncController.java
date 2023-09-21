package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.AsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@RestController
@RequestMapping("/async")
public class AsyncController {

    AsyncService asyncService;

    public AsyncController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping
    public Object async() throws ExecutionException, InterruptedException, TimeoutException {
        System.out.println("main: " + Thread.currentThread().getName());
        CompletableFuture<String> async = asyncService.async();
        System.out.println(async);
        return async.get(3, TimeUnit.SECONDS);
    }
}
