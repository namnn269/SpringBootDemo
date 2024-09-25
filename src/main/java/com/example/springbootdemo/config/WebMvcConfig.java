package com.example.springbootdemo.config;

import com.example.springbootdemo.config.interceptor.TimezoneInterceptor;
import com.example.springbootdemo.config.url_param_handler.SimpleQueryParamHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SimpleQueryParamHandler());
//        resolvers.add(new MyCustomQueryParamHandler(false));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimezoneInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(zoneIdFormatter());
//        registry.addConverter(zoneIdConverter());
    }

    private Formatter<ZoneId> zoneIdFormatter() {
        return new Formatter<>() {
            @Override
            public ZoneId parse(String text, Locale locale) throws ParseException {
                return text.equals("Asia/Tokyo") ? null : ZoneId.of(text);
            }

            @Override
            public String print(ZoneId object, Locale locale) {
                return object.toString();
            }
        };
    }

    private Converter<String, ZoneId> zoneIdConverter() {
        return new Converter<String, ZoneId>() {
            @Override
            public ZoneId convert(String source) {
                return source.equals("Asia/Tokyo") ? null : ZoneId.of(source);
            }
        };
    }
}
