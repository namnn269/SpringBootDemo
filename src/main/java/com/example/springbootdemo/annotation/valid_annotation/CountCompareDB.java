package com.example.springbootdemo.annotation.valid_annotation;

import com.example.springbootdemo.validate_annotation.ValidatorInDB;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatorInDB.class)
public @interface CountCompareDB {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "count must be greater than %d";
}
