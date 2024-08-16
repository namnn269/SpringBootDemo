package com.example.springbootdemo.validator_service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidatorUsingValidatorJakarta {

    private final Validator validator;

    public ValidatorUsingValidatorJakarta(Validator validator) {
        this.validator = validator;
    }

    public <T> Set<ConstraintViolation<T>> validateOne(T obj) {
        Set<ConstraintViolation<T>> errorSet = validator.validate(obj);
        for (ConstraintViolation<T> error : errorSet) {
            error.getMessage();
        }
        return errorSet;
    }

}
