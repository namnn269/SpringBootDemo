package com.example.springbootdemo.multi_threading.ex1;

import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.ThreadLocalRandom;

public class ProducerConsumerMain {
    public static void main(String[] args) {
        ProducerConsumer producerConsumer = new ProducerConsumer();

        Thread consumerThread = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
//                producerConsumer.consume();
//            }
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                producerConsumer.consume();
            }
        });

        Thread producerThread = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
//                producerConsumer.produce();
//            }
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                producerConsumer.produce();
            }
        });

        consumerThread.start();
        producerThread.start();
    }
}
