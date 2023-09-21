package com.example.springbootdemo.validate_annotation;

import com.example.springbootdemo.annotation.valid_annotation.StartString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartStringValidator implements ConstraintValidator<StartString, String> {

    private String userValue;

    @Override
    public void initialize(StartString constraintAnnotation) {
        userValue = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty() || !value.startsWith(userValue)) {
            context.buildConstraintViolationWithTemplate("String must start with '".concat(userValue).concat("'"))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
