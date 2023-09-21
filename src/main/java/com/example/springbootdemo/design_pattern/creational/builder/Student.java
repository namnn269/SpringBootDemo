package com.example.springbootdemo.design_pattern.creational.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Student {
    private String name;
    private int age;

    @Override
    public String toString() {
        return "Student{" +
                "name=" + name +
                ", age=" + age +
                '}';
    }
}
