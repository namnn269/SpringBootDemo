package com.example.springbootdemo.listner.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//@Component
public class TestTopicListener {

    @KafkaListener(
            topics = "test-topic",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "test-group",
            concurrency = "2",
            autoStartup = "true"
    )
    public void handle(List<ConsumerRecord<String, Map<String, Object>>> records) {
        records.forEach(System.out::println);
    }
}
