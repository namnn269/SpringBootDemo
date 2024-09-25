package com.example.springbootdemo.config;

import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class ValidationConfig {

    private final MessageSource messageSource;
    private final AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Bean
    public ValidatorFactory validatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.setMessageInterpolator(getMessageInterpolator());
        validatorFactoryBean.setConstraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory));
        return validatorFactoryBean;
    }

    // priority
    @Bean
    public ValidatorFactory validatorFactory() {
        try (ValidatorFactory validatorFactory = Validation
                .byDefaultProvider()
                .configure()
                .clockProvider(getClockProvider())
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .messageInterpolator(getMessageInterpolator())
                .parameterNameProvider(getParameterNameProvider()) // chỉ có tác dụng khi validate method, constructor
                .buildValidatorFactory()) {
            return validatorFactory;
        }
    }

    private ClockProvider getClockProvider() {
        return () -> Clock.system(ZoneIdHolder.getZoneId());
    }

    private MessageInterpolator getMessageInterpolator() {
        ResourceBundleLocator resourceBundleLocator = new MessageSourceResourceBundleLocator(messageSource);
        MessageInterpolator hibernateMsgInterpolator = new ResourceBundleMessageInterpolator(resourceBundleLocator);
        return new MyMessageInterpolator(hibernateMsgInterpolator);
    }

    private ParameterNameProvider getParameterNameProvider() {
        return new ParameterNameProvider() {

            @Override
            public List<String> getParameterNames(Constructor<?> constructor) {
                return getParameterNames(constructor.getParameters());
            }

            @Override
            public List<String> getParameterNames(Method method) {
                return getParameterNames(method.getParameters());
            }

            private List<String> getParameterNames(Parameter[] parameters) {
                List<String> parameterNames = new ArrayList<>();

                for (Parameter parameter : parameters) {
                    parameterNames.add("_" + parameter.getName());
                }

                return parameterNames;
            }
        };
    }

    @Bean
    public Validator validator(ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
    }

    @RequiredArgsConstructor
    public static class MyMessageInterpolator implements MessageInterpolator {

        private final MessageInterpolator messageInterpolator;

        @Override
        public String interpolate(String messageTemplate, Context context) {
            messageTemplate = " message handled: 1. " + messageTemplate;
            return messageInterpolator.interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
        }

        @Override
        public String interpolate(String messageTemplate, Context context, Locale locale) {
            messageTemplate = " message handled: 2. " + messageTemplate;
            return messageInterpolator.interpolate(messageTemplate, context, locale);
        }
    }
}
