package com.example.springbootdemo.convert_object;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TargetObject<A, B> {
    private String name;
    private A a;
    private B b;
}
