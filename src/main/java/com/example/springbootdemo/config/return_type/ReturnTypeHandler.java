package com.example.springbootdemo.config.return_type;

import com.example.springbootdemo.dto.ObjectDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReturnTypeHandler extends RequestResponseBodyMethodProcessor {

    public ReturnTypeHandler(List<HttpMessageConverter<?>> converters,
                             ContentNegotiationManager contentNegotiationManager) {
        super(converters, contentNegotiationManager);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getParameterType().equals(ObjectDto.class);
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
        System.out.println("check return type in ReturnTypeHandler.java: "+returnValue);
        if (returnValue instanceof ObjectDto dto) {
            dto.setObject(LocalDateTime.now());
        }
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
