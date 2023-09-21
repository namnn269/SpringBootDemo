package com.example.springbootdemo.validate_annotation;

import com.example.springbootdemo.annotation.valid_annotation.MaxListSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class MaxListSizeValidator implements ConstraintValidator<MaxListSize, List<?>> {

    private int maxSize;

    @Override
    public void initialize(MaxListSize constraintAnnotation) {
        maxSize = constraintAnnotation.size();
    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        if (list == null || list.size() <= maxSize) {
//            context.buildConstraintViolationWithTemplate("Size must >= " + maxSize)
//                    .addConstraintViolation();
            return false;
        } else
            return true;
    }
}
