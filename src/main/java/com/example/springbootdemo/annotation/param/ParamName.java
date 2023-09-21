package com.example.springbootdemo.annotation.param;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamName {
    String value();
}
