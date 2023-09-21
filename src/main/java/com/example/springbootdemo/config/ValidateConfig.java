package com.example.springbootdemo.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@Configuration
public class ValidateConfig {
    @Bean
    public Validator localValidatorFactoryBean(AutowireCapableBeanFactory autowireCapableBeanFactory) {

        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .buildValidatorFactory();
        return validatorFactory.getValidator();
//        return new LocalValidatorFactoryBean();
    }

//
//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
//        processor.setValidator(localValidatorFactoryBean());
//        return processor;
//    }
}
