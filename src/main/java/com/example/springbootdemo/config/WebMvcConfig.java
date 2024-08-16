package com.example.springbootdemo.config;

import com.example.springbootdemo.config.url_param_handler.SimpleQueryParamHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SimpleQueryParamHandler());
//        resolvers.add(new MyCustomQueryParamHandler(false));
    }

}
