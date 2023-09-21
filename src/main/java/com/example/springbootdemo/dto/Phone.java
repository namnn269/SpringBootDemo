package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    private Integer code;
    private String model;
    private Date releasedDate;
}
