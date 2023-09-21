package com.example.springbootdemo.config.requestparam;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ControllerParamConfig {

//    @Bean
    public BeanPostProcessor beanPostProcessor(){
        return new RequestParamPostProcessor();
    }
}
