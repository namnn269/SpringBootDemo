package com.example.springbootdemo.annotation.valid_annotation;

import com.example.springbootdemo.validate_annotation.MaxListSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxListSizeValidator.class)
public @interface MaxListSize {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "Max size must be less than {size}";

    int size() default 100;
}
