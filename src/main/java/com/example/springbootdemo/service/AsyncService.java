package com.example.springbootdemo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class AsyncService {
    @Async
    public CompletableFuture<String> async() {
        try {
            Thread.sleep(5000);
            System.out.println("async: " + Thread.currentThread().getName());
            return CompletableFuture.supplyAsync(() -> "ok from async method");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

