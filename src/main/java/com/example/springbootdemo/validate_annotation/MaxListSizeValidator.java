package com.example.springbootdemo.validate_annotation;

import com.example.springbootdemo.annotation.valid_annotation.MaxListSize;
import com.example.springbootdemo.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaxListSizeValidator implements ConstraintValidator<MaxListSize, List<?>> {

    private int maxSize;
    private final UserRepository userRepository;

    @Autowired
    public MaxListSizeValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(MaxListSize constraintAnnotation) {
        maxSize = constraintAnnotation.size();
    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        System.out.println("==> " + userRepository);
        if (list == null || list.size() <= maxSize) {
//            context.buildConstraintViolationWithTemplate("Size must >= " + maxSize)
//                    .addConstraintViolation();
            return false;
        } else
            return true;
    }
}
