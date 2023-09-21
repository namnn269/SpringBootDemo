package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private String email;
    private String name;
    private String gender;
    private String address;
    private String phone;
    private Integer age;
}
