package com.example.springbootdemo.controller;

import com.example.springbootdemo.enums.Color;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/enum-param")
public class EnumController {

    @GetMapping
    public Object get(@RequestParam Color color) {
        System.out.println(color);
        return color.toString();
    }

    @PostMapping
    public Object post(@RequestBody ColorCollection color) {
        System.out.println(color);
        return color;
    }

    @Data
    @NoArgsConstructor
    static class ColorCollection {
        String name;
        Color color;
    }
}

