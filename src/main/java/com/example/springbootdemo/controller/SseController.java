package com.example.springbootdemo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

@RestController
public class SseController {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Map<String, Collection<SseEmitter>> idToEmitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc() {
        System.out.println("BEGIN CONTROLLER");
        SseEmitter emitter = new SseEmitter();
        executorService.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("sending: " + i);
                    emitter.send(SseEmitter.event().name("my_data").data("My Message " + i));
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

    @GetMapping(value = "/sse/multi-connection", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc(@RequestParam(defaultValue = "1") String id) {
        System.out.println("BEGIN CONTROLLER");

        boolean exists = idToEmitters.containsKey(id);

        Collection<SseEmitter> sseEmitters = idToEmitters.computeIfAbsent(id, k -> new ConcurrentLinkedQueue<>());
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitters.add(sseEmitter);
        sseEmitter.onCompletion(() -> sseEmitters.remove(sseEmitter));

        if (!exists) {
            executorService.execute(() -> {
                for (int i = 0; i < 10; i++) {
                    int j = i;
                    try {
                        // simulating job
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sseEmitters.forEach(emitter -> {
                        System.out.println("sending: " + j);
                        try {
                            emitter.send(SseEmitter.event().name("my_data").data(String.format("My Message to id %s: %s", id, j)));
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            sseEmitters.remove(emitter);
                        }
                    });
                }
                System.out.println("END");
                // Kết thúc kết nối SSE
                sseEmitters.forEach(ResponseBodyEmitter::complete);
                idToEmitters.remove(id);
            });
        }
        System.out.println("END CONTROLLER");
        return sseEmitter;
    }

    @GetMapping()
    public ModelAndView index() {
        System.out.println("in index controller");
        return new ModelAndView("myview2.html");
    }
}
