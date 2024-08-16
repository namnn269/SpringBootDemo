package com.example.springbootdemo.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class SseController {
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc() {
        System.out.println("BEGIN CONTROLLER");
        SseEmitter emitter = new SseEmitter();
        executorService.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("sending: " + i);
                    emitter.send(SseEmitter.event().name("message").data("Message " + i));
                    Thread.sleep(1000);
                }
                System.out.println("END");
                emitter.complete(); // Kết thúc kết nối SSE
            } catch (InterruptedException | IOException e) {
                System.out.println("catch error: " + e.getMessage());
                emitter.completeWithError(e);
            }
        });
        System.out.println("END CONTROLLER");
        return emitter;
    }

    @GetMapping()
    public ModelAndView index() {
        System.out.println("in index controller");
        return new ModelAndView("myview2.html");
    }
}
