package com.example.springbootdemo.annotation.valid_annotation;

import com.example.springbootdemo.validate_annotation.StartStringValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartStringValidator.class)
public @interface StartString {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message() default "String must start with 'a'";
    String value() default "a";
}
