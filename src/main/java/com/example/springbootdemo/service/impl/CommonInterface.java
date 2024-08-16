package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dto.Customer;

public interface CommonInterface {
    Object executeCommonInterface(int i);

    default Object executeCommonDefaultInterface(int i) {
        return new Customer(i, "common default interface");
    }
}
