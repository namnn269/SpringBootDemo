package com.example.springbootdemo.validator_service;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ListFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("in list form validator");
        System.out.println("===> " + errors);
        List<?> list = (List<?>) target;
        for (Object o : list) {
            System.out.println(o);
        }
    }
}
