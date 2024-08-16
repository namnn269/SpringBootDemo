package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.annotation.MyAnnotationOnClass;
import com.example.springbootdemo.annotation.MyAnnotationOnMethod;
import com.example.springbootdemo.dto.Customer;

@MyAnnotationOnClass
public abstract class AbstractAopService extends CommonAbstract implements AopService {
    @MyAnnotationOnMethod
    public abstract Customer executeOverrideAbstract(int i);

    @MyAnnotationOnMethod
    public Customer executeNormalInAbstract(int i) {
        return new Customer(i, "normal in abstract");
    }

    @MyAnnotationOnMethod
    public Customer executeOverrideNormalInAbstract(int i) {
        return new Customer(i, "override normal in abstract");
    }

}
