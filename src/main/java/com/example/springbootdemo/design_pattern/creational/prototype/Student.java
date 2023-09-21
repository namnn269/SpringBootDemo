package com.example.springbootdemo.design_pattern.creational.prototype;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class Student implements Cloneable {
    private String name;
    private LocalDateTime dob;

    @Override
    public Student clone() {
        try {
            Student clone = (Student) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return name + " --> " + dob;
    }
}
