package com.example.springbootdemo.config.requestparam;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.LinkedList;
import java.util.List;

public class RequestParamPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter handlerAdapter) {

            List<HandlerMethodArgumentResolver> argumentResolvers = handlerAdapter.getArgumentResolvers();
            if (argumentResolvers == null) argumentResolvers = new LinkedList<>();
            argumentResolvers.add(new RequestMethodParamResolver(false));
            handlerAdapter.setArgumentResolvers(argumentResolvers);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
