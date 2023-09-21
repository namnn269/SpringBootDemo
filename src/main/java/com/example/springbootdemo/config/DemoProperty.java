package com.example.springbootdemo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties(DemoProperty.class)
@ConfigurationProperties(prefix = "demo.okok")
@PropertySource("classpath:application-dev.properties")
@Component
@Data
@NoArgsConstructor
public class DemoProperty {
    private String lol1;
    private String lol2;
    private String lol3;
    private String lol4;
    private String lol5;
}
