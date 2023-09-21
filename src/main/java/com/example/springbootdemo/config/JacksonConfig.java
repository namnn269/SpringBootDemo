package com.example.springbootdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class JacksonConfig {

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // Cấu hình module hỗ trợ Java 8 Date/Time API (java.time)
//        objectMapper.registerModule(new JavaTimeModule());
//
//        // Vô hiệu hóa chuyển đổi múi giờ tự động thành UTC
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        return objectMapper;
//    }
}
