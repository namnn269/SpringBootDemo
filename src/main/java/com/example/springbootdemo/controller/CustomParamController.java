package com.example.springbootdemo.controller;

import com.example.springbootdemo.config.url_param_handler.ConvertibleParam;
import com.example.springbootdemo.dto.ObjectDto;
import com.example.springbootdemo.param_in_object.CustomParam;
import com.example.springbootdemo.param_in_object.DemoParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CustomParamController {

    @GetMapping("/my-custom-param")
    public Object get(@ConvertibleParam CustomParam param) {
        System.out.println(param.getMyFile() != null);
        System.out.println(param.getMyFile2() != null);
        return param;
    }

    @GetMapping("/param-in-object")
    public Object demo(DemoParam demoParam) {
        System.out.println(demoParam);
        return demoParam;
    }

    @GetMapping("/check-return-value")
    public ObjectDto get() {
        return new ObjectDto("lol");
    }

}
