package com.example.springbootdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// solution 1
//@Configuration
//public class DateConfig extends WebMvcConfigurationSupport {
//    @Bean
//    @Override
//    public FormattingConversionService mvcConversionService() {
//        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
//
//        DateFormatterRegistrar dateRegistrar = new DateFormatterRegistrar();
//        dateRegistrar.setFormatter(new DateFormatter("yyyy-MM-dd'P'HH:mm:ss"));
//        dateRegistrar.registerFormatters(conversionService);
//
//        return conversionService;
//    }
//}

// solution 2
//@Component
//class DateFormatter2 implements Formatter<Date> {
//
//    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'W'HH:mm:ss");
//
//    @Override
//    public Date parse(String text, Locale locale) throws ParseException {
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return dateFormat.parse(text);
//    }
//
//    @Override
//    public String print(Date date, Locale locale) {
//        return dateFormat.format(date);
//    }
//}
//
//@Configuration
// class DateConfig2 implements WebMvcConfigurer {
//
//    @Autowired
//    private DateFormatter2 dateFormatter;
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(dateFormatter);
//    }
//}

// solution 3
//@RestControllerAdvice
//class DateControllerAdvice {
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'X'HH:mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }
//}
