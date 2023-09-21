package com.example.springbootdemo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@ConfigurationProperties(prefix = "map")
@EnableConfigurationProperties(MapProperties.class) // can discard this line
@Component
@Data
@NoArgsConstructor
public class MapProperties {

    private Map<String, Properties> mapProps;
}
