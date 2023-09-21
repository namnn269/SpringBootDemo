package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
    @Autowired
    @Qualifier(value = "blo")
    private Customer customer;

    @GetMapping
    public ResponseEntity<?> lol() {
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }


}
