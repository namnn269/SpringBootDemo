package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ReportDto {
    private Integer id;
    private String name;
    private Integer age;
    private LocalDate dob;
}
