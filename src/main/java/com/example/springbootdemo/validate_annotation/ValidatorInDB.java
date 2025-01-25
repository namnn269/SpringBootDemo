package com.example.springbootdemo.validate_annotation;

import com.example.springbootdemo.annotation.valid_annotation.CountCompareDB;
import com.example.springbootdemo.config.ZoneIdHolder;
import com.example.springbootdemo.infrastructure.database.logicom.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ValidatorInDB implements ConstraintValidator<CountCompareDB, Integer> {

    private final UserRepository userRepository;

    public ValidatorInDB(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(Integer valueInForm, ConstraintValidatorContext context) {

        String userRepo = userRepository != null ? "userRepo is NOT NULL" : "userRepo is NULL";

        System.out.printf("count user in DB: %s%n", userRepo);

        if (valueInForm == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("count must be not null LOL")
                    .addConstraintViolation();
            return false;
        } else if (valueInForm <= 12) {
            context.disableDefaultConstraintViolation();
            HibernateConstraintValidatorContext validatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
            validatorContext
                    .addMessageParameter("countValue", 12)
                    .addMessageParameter("timeZone", ZonedDateTime.now(ZoneIdHolder.getZoneId()))
                    .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("aaa")
                    .addPropertyNode("ccc").inContainer(Map.class, 1).inIterable().atKey("ddd")
                    .addPropertyNode("eee").inContainer(List.class, 1).inIterable().atIndex(3)
                    .addBeanNode().inContainer(List.class, 1).inIterable().atKey("fff")
                    .addConstraintViolation();

            validatorContext
                    .addMessageParameter("foo", 12)
                    .buildConstraintViolationWithTemplate("${foo}")
                    .addConstraintViolation();
            return false;
        } else return true;
    }

}
