package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.service.AnnotationDao;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AopServiceConcreteImpl extends AopServiceImpl {
    public AopServiceConcreteImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  AnnotationDao annotationDao) {
        super(namedParameterJdbcTemplate, annotationDao);
    }
}
