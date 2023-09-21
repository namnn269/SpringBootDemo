package com.example.springbootdemo.dto;

import jakarta.annotation.Nonnull;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @NonNull
    private Integer id;
    @NonNull
    private String name;
    private int age;
}
