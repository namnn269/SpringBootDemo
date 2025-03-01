package com.example.springbootdemo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kafka")
@AllArgsConstructor
public class KafkaController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/push")
    public Object pushToTopic(@RequestBody KafkaData data) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(
                data.getTopic(),
                data.getPartition(),
                data.getKey(),
                data.getValue());
        CompletableFuture<SendResult<String, Object>> result = kafkaTemplate.send(producerRecord);
        return result.thenApply(s -> "Push success").exceptionally(Throwable::getMessage);
    }

    @Getter
    @Setter
    public static class KafkaData {
        private String topic = "test-topic";
        private String key;
        private int partition;
        private Object value;
    }
}
