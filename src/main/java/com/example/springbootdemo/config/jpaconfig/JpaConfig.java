package com.example.springbootdemo.config.jpaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "myAuditorAware")
public class JpaConfig {

    @Bean(name = "myAuditorAware")
    public AuditorAware<Integer> auditorAware() {
        return new AuditorAwareImpl();
    }
}
