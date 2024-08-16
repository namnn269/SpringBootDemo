package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.ValidForm;
import com.example.springbootdemo.validator_service.ValidatorExtendsValidatorBeanSpring;
import com.example.springbootdemo.validator_service.ValidatorUsingValidatorJakarta;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/validate")
//@Validated
public class ValidationController {

    final ValidatorExtendsValidatorBeanSpring validatorBeanSpring;
    final ValidatorUsingValidatorJakarta validatorJakarta;
    final Validator validator;
    final ValidatorFactory validatorFactory;

    public ValidationController(ValidatorExtendsValidatorBeanSpring validatorBeanSpring,
                                ValidatorUsingValidatorJakarta validatorJakarta,
                                Validator validator,
                                ValidatorFactory validatorFactory) {
        this.validatorBeanSpring = validatorBeanSpring;
        this.validatorJakarta = validatorJakarta;
        this.validator = validator;
        this.validatorFactory = validatorFactory;
    }
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
        Set<ConstraintViolation<ValidForm>> constraintViolations1 = validator.validate(form);
        Set<ConstraintViolation<ValidForm>> constraintViolationsGroup1 = validator.validate(form, ValidForm.GroupClass1.class);
        Set<ConstraintViolation<ValidForm>> constraintViolationsGroup2 = validator.validate(form, ValidForm.GroupClass2.class);
        Set<ConstraintViolation<ValidForm>> constraintViolationsGroup12 = validator.validate(form, ValidForm.GroupClass1.class, ValidForm.GroupClass2.class);
        Set<ConstraintViolation<ValidForm>> constraintViolations = validatorJakarta.validateOne(form);
        List<String> collect = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        constraintViolations.stream()
                .findFirst()
                .ifPresent(cv -> {
                    Annotation annotation = cv.getConstraintDescriptor().getAnnotation();
                    if (annotation instanceof NotNull castedAnnotation) {
                        System.out.println(castedAnnotation + "is type of NotNull");
                        System.out.println(castedAnnotation.message());
                    }
                });
        System.out.println("===> " + constraintViolations1.size()
                + " == group 1: " + constraintViolationsGroup1.size()
                + " == group 2: " + constraintViolationsGroup2.size()
                + " == group 12: " + constraintViolationsGroup12.size());
        return collect;
    }


    @PostMapping("/custom-annotation")
    public Object post(@RequestBody ValidForm form) {
        return form;
    }

}
