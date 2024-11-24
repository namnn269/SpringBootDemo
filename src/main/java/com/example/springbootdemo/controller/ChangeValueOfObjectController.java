package com.example.springbootdemo.controller;

import com.example.springbootdemo.annotation.serializer.ChangeValue;
import com.example.springbootdemo.annotation.serializer.Hidden;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/change-value")
public class ChangeValueOfObjectController {
    int i = 0;

    @GetMapping
    public Object get() {
        Dog res = Dog.builder()
                .totalMoney(1000)
                .isHidden(i++ % 2 == 0)
                .pens(List.of(
                        new Pen(i % 2 == 0, 5000, "pen 1"),
                        new Pen(i % 2 == 0, 4000, "pen 2")
                ))
                .pen( new Pen(i % 2 == 0, 3000, "pen 3"))
                .build();
        return ResponseEntity.ok(res);
    }

    @Data
    @Builder
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Dog {
        @Hidden
        private Boolean isHidden;
        @ChangeValue(ChangeValue.ValueType.NUMBER)
        private int totalMoney;
        private List<Pen> pens;
        private Map<Integer,Pen> map;
        private Pen pen;
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pen {
        @Hidden
        private Boolean isHidden;
        @ChangeValue(ChangeValue.ValueType.NUMBER)
        private int totalMoney;
        @ChangeValue(ChangeValue.ValueType.STRING)
        private String fullName;
    }

}
