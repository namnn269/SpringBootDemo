package com.example.springbootdemo.validator_service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.lang.reflect.Field;
import java.util.*;

/* Nên sử dụng để có nhiều chi tiết hơn */
@Service
public class ValidatorExtendsValidatorBeanSpring extends CustomValidatorBean {

    public boolean hasError(Object target) {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, target.getClass().getName());
        // run validate
        super.validate(target, result);
        return result.hasErrors();
    }

    public Map<String, List<String>> validateAndGetMessages(Object target) {

        BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, target.getClass().getName());

        // run validate
        super.validate(target, result);

        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, List<String>> errorMap = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            if (errorMap.containsKey(field)) {
                List<String> strings = errorMap.get(field);
                String defaultMessage = fieldError.getDefaultMessage();
                strings.add(defaultMessage);
            } else {
                List<String> values = new ArrayList<>();
                values.add(fieldError.getDefaultMessage());
                errorMap.put(field, values);
            }
        }
        return errorMap;
    }

    public Map<?, ?> validateCollection(Collection<?> collection, String field) {
        Map<Object, Map<String, List<String>>> resultMap = new HashMap<>();
        for (Object o : collection) {
            Map<String, List<String>> map = this.validateAndGetMessages(o);
            try {
                Field fieldKey = o.getClass().getDeclaredField(field);
                fieldKey.setAccessible(true);
                Object key = fieldKey.get(o);
                if (key == null)
                    throw new RuntimeException("key must not be empty");
                resultMap.put(key, map);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        return resultMap;
    }
}
