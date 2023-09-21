package com.example.springbootdemo.validator_service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

/* khi sử dụng validator Jakarta này thì không biết được trường nào bị invalid => sử dụng CustomValidatorBean của Spring */
@Component
public class ValidatorUsingValidatorJakarta {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public <T> Set<ConstraintViolation<T>> validateOne(T obj) {
        Set<ConstraintViolation<T>> errorSet = validator.validate(obj);
        for (ConstraintViolation<T> error : errorSet) {
            error.getMessage();
        }
        return errorSet;
    }

}
