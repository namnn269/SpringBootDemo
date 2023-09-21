package com.example.springbootdemo.design_pattern.creational.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Student student = new Student("asd", LocalDateTime.now());
        Student clone = student.clone();
        System.out.println(student == clone);
        System.out.println(student);
        System.out.println(clone);
    }
}

