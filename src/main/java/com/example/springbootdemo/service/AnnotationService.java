package com.example.springbootdemo.service;

import com.example.springbootdemo.annotation.MyMethodAnnotation;
import com.example.springbootdemo.dto.Customer;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {

    @MyMethodAnnotation
    public Customer get(int id){
        return new Customer(id,"in annotation");
    }

}
