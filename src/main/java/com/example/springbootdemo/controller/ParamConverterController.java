package com.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Map;

@RestController
@RequestMapping(value = "/param-converter")
public class ParamConverterController {

    @GetMapping
    public Object convert(@RequestParam(required = false, value = "zoneId") ZoneId zoneId,
                              @RequestParam(required = false, value = "zoneId") String zoneIdStr) {
        return Map.of(
                "zoneId", zoneId,
                "zoneIdStr", zoneIdStr);
    }

}
