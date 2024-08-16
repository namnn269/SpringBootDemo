package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.annotation.MyAnnotationOnMethod;
import com.example.springbootdemo.dto.Customer;
import com.example.springbootdemo.service.impl.CommonInterface;

public interface AopService extends CommonInterface {
    @MyAnnotationOnMethod
    Customer executeOverrideInterface(int id);
    @MyAnnotationOnMethod
    default Customer executeDefaultInInterface(int i) {
        return new Customer(i, "default in interface");
    }
    @MyAnnotationOnMethod
    default Customer executeOverrideDefaultInInterface(int i) {
        return new Customer(i, "override default in interface");
    }

}
