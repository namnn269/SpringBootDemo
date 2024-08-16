package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.annotation.MyAnnotationOnMethod;
import com.example.springbootdemo.dto.AopDto;
import com.example.springbootdemo.dto.AopParam;
import com.example.springbootdemo.dto.Customer;
import com.example.springbootdemo.repository.MMaterialRepository;
import com.example.springbootdemo.service.AnnotationDao;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AopServiceImpl extends AbstractAopService {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    AnnotationDao annotationDao;
    MMaterialRepository materialRepo;

    public AopServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                          AnnotationDao annotationDao) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.annotationDao = annotationDao;
    }

    @Override
    public Customer executeOverrideInterface(int id) {
        return new Customer(id, "override interface");
    }

    @Override
    public Customer executeOverrideDefaultInInterface(int i) {
        return new Customer(i, "override default method in interface");
    }

    @Override
    public Customer executeOverrideAbstract(int id) {
        return new Customer(id, "override abstract");
    }

    @Override
    public Customer executeOverrideNormalInAbstract(int i) {
        return new Customer(i, "override normal method in abstract");
    }

    @Override
    public Customer executeCommonMethod(int i) {
        return new Customer(i, "override common method in abstract");
    }

    @Override
    public Customer executeCommonInterface(int i) {
        return new Customer(i, "override common method in interface");
    }

    public Customer executeNormalMethodInClass(int id) {
        return new Customer(id, "normal");
    }

    public Customer doArgsInClass(int i,
                                  AopDto param,
                                  String name) {
        return new Customer(i, name);
    }

    public Customer doAnnoArgsInClass(int i,
                                      AopDto param,
                                      AopDto param2,
                                      String name) {
        return new Customer(i, name);
    }

    @MyAnnotationOnMethod
    public Customer doAnnotationOnMethod(int id) {
        return new Customer(id, "annotation in method");
    }
}
