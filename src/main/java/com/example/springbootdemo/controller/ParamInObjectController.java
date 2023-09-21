package com.example.springbootdemo.controller;

import com.example.springbootdemo.param_in_object.DemoParam;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("param-in-object")
public class ParamInObjectController {

    @GetMapping
    public Object demo(DemoParam demoParam) {
        System.out.println(demoParam);
        return demoParam;
    }

}
