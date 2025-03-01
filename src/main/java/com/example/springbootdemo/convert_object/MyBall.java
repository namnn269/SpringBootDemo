package com.example.springbootdemo.convert_object;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MyBall<S> {
    private S shape;
    private double diameter;
}
