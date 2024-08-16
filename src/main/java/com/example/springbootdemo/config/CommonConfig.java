package com.example.springbootdemo.config;

import com.example.springbootdemo.config.return_type.ReturnTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CommonConfig {

    @Autowired
    public void setup(RequestMappingHandlerAdapter adapter) {
        List<HandlerMethodReturnValueHandler> handlers = adapter.getReturnValueHandlers();
        handlers = new ArrayList<>(handlers != null ? handlers : new ArrayList<>());
        List<HttpMessageConverter<?>> messageConverters = adapter.getMessageConverters();
        ReturnTypeHandler returnTypeHandler = new ReturnTypeHandler(messageConverters, new ContentNegotiationManager());
        handlers.add(0, returnTypeHandler);
        adapter.setReturnValueHandlers(handlers);
    }
}
