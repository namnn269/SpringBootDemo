package com.example.springbootdemo.dto;

import com.example.springbootdemo.annotation.AopArgs;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@AopArgs
public class AopParam implements AopDto {
    private Integer i;
    private String name;
}
