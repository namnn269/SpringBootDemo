package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.ValidForm;
import com.example.springbootdemo.validator_service.ValidatorExtendsValidatorBeanSpring;
import com.example.springbootdemo.validator_service.ValidatorUsingValidatorJakarta;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/validate")
//@Validated
public class ValidateController {

    @Autowired
    ValidatorExtendsValidatorBeanSpring validatorBeanSpring;
    @Autowired
    ValidatorUsingValidatorJakarta validatorJakarta;


//    @Autowired
//    private ListFormValidator formValidator;
//
//    @InitBinder
//    private void initBinder(WebDataBinder binder) {
//        binder.addValidators(formValidator);
//    }

    @PostMapping
    public Object validate(@RequestBody @Valid ValidForm form) {
        return form;
    }


    @PostMapping(value = "/validate-with-spring-bean-one")
    public Object validateOne(@RequestBody ValidForm form) {
        Map<?, ?> errorMap = validatorBeanSpring.validateAndGetMessages(form);
        return errorMap;
    }

    @PostMapping(value = "/validate-with-spring-bean-list")
    public Object validateList(@RequestBody List<ValidForm> forms) {
        Map<?, ?> errorMap = validatorBeanSpring.validateCollection(forms, "recordId");
        return errorMap;
    }

    @PostMapping(value = "/validate-with-jakarta-one")
    public Object validateOneJakarta(@RequestBody ValidForm form) {

        List<ConstraintViolation<ValidForm>> constraintViolations = validatorJakarta.validateOne(form).stream().collect(Collectors.toList());
        List<String> collect = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return collect;
    }



    @PostMapping("/custom-annotation")
    public Object post(@RequestBody ValidForm form) {
        return form;
    }

}
