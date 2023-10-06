package com.example.springbootdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/escape-char")
public class EscapeCharController {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Object nativeQuery() {
        return null;
    }

    public static void main(String[] args) {
        System.out.println("'poo".replaceAll("'", "\\\\'"));
        System.out.println("\\".replaceAll("\\\\", "\\\\\\\\\\\\\\\\"));
    }
}
