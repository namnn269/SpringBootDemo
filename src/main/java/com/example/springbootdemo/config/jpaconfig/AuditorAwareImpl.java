package com.example.springbootdemo.config.jpaconfig;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Integer> {
    private int id = 0;

    @Override
    public Optional<Integer> getCurrentAuditor() {
        return Optional.of(id++);
    }
}
