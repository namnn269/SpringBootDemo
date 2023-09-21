package com.example.springbootdemo.controller;

import com.example.springbootdemo.config.DemoProperty;
import com.example.springbootdemo.config.MapProperties;
import com.example.springbootdemo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/properties")
public class PropertyController {

    @Autowired
    private DemoProperty demoProperty;

    @Autowired
    private MapProperties mapProperties;

    @GetMapping
    public ResponseEntity<?> lol() {
        System.out.println("in properties controller");
        return new ResponseEntity<>(demoProperty, HttpStatus.OK);
    }
    @GetMapping(value = "/map")
    public ResponseEntity<?> map() {
        System.out.println("in map properties controller");
        return new ResponseEntity<>(mapProperties, HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<?> customers(){
        return new ResponseEntity<>(new Customer(1,"customer1"), HttpStatus.OK);
    }

}
