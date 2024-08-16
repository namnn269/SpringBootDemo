package com.example.springbootdemo.controller;

import com.example.springbootdemo.service.ExtendEntityManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/using-extend")
public class ExtendEntityManagerController {
    private final ExtendEntityManagerService extendEntityManagerService;

    public ExtendEntityManagerController(ExtendEntityManagerService extendEntityManagerService) {
        this.extendEntityManagerService = extendEntityManagerService;
    }

    @GetMapping("/use-entity-manager")
    public Object useEntityManager() {
        return extendEntityManagerService.useEntityManager();
    }

    @GetMapping("/use-named-parameter-jdbc-template")
    public Object useNamedParameterJdbcTemplate() {
        return extendEntityManagerService.useNamedParameterJdbcTemplate();
    }
}
