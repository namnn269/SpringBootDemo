package com.example.springbootdemo.dto;

import com.example.springbootdemo.annotation.valid_annotation.CountCompareDB;
import com.example.springbootdemo.annotation.valid_annotation.MaxListSize;
import com.example.springbootdemo.annotation.valid_annotation.StartString;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidForm {
    private Integer recordId;

    @NotNull(message = "Name must be not null :vvv")
    @NotEmpty
    @StartString
    private String name;
    @NotNull
    @Min(0)
    @Max(10)
    private Integer age;

    @CountCompareDB
    private Integer count;

    @MaxListSize(size = 666)
    List<Integer> list;

}
