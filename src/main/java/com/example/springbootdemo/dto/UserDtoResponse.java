package com.example.springbootdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResponse {
    private Integer id;
    private String email;
    private Integer otherField;
}
