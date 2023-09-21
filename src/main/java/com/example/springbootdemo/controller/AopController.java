package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.Customer;
import com.example.springbootdemo.dto.UserDto;
import com.example.springbootdemo.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/aop")
public class AopController {

    @Autowired
    private AnnotationService annotationService;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "id", defaultValue = "1") int id,
                                 @RequestParam(name = "name", defaultValue = "default customer") String name) {
        if (id == 10) throw new RuntimeException("in get method, no customer having id equals to 10");
        Customer customer = new Customer(id, name + " " + id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping(value = "/around1")
    public ResponseEntity<?> around1(@RequestParam(defaultValue = "1") int id,
                                     @RequestParam(defaultValue = "customer") String name) {
        if (id == 10) throw new RuntimeException("in around method 1, no customer having id equals to 10");
        Customer customer = new Customer(id, name + " " + id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping(value = "/around2")
    public ResponseEntity<?> around2(@RequestParam(defaultValue = "1") int id,
                                     @RequestParam(defaultValue = "customer") String name,
                                     @RequestParam(defaultValue = "0") int age) {
        if (id == 10) throw new RuntimeException("in around method 2, no customer having id equals to 10");
        Customer customer = new Customer(id, name + " " + id, age);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping(value = "/annotation")
    public ResponseEntity<?> annotation(@RequestParam(defaultValue = "1") int id) {
        return new ResponseEntity<>(annotationService.get(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody UserDto form,
                                  @RequestParam String size,
                                  @RequestParam String address) {
        return new ResponseEntity<>(form, HttpStatus.OK);
    }

}
