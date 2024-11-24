package com.example.springbootdemo.annotation.serializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeValue {

    ValueType value();

    enum ValueType {
        NUMBER, STRING
    }
}
