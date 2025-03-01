package com.example.springbootdemo.annotation.convert_object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ObjectConverter {
    Class<?> value();
    GenericConverterLevel1[] genericLevel1() default {};
}
