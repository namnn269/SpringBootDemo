package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dto.Customer;

public abstract class CommonAbstract {
    public abstract Object executeCommonMethod(int i);

    public Object executeNormalCommonMethod(int i) {
        return new Customer(i, "common method in abstract");
    }
}
