package com.example.springbootdemo.design_pattern.creational.builder;

public class Main {
    public static void main(String[] args) {
        Student.StudentBuilder studentBuilder = new Student.StudentBuilder();
        Student student = studentBuilder.name("as").age(4).build();
        System.out.println(studentBuilder);
        System.out.println(student);
    }
}
