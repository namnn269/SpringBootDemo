package com.example.springbootdemo.utils;

import com.example.springbootdemo.main.Main;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPair<L, R> {

    private L left;
    private R right;

    public static <L, R> MyPair<L, R> of(L left, R right) {
        return new MyPair<>(left, right);
    }

    @Override
    public String toString() {
        return "(" + left + "," + right + ")";
    }
}
