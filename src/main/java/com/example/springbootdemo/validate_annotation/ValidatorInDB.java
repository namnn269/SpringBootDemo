package com.example.springbootdemo.validate_annotation;

import com.example.springbootdemo.annotation.valid_annotation.CountCompareDB;
import com.example.springbootdemo.repository.UserRepository;
import com.example.springbootdemo.utils.BeanFactory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

@Service
public class ValidatorInDB implements ConstraintValidator<CountCompareDB, Integer> {

    private UserRepository userRepository;


    @Override
    public boolean isValid(Integer valueInForm, ConstraintValidatorContext context) {
        if (userRepository == null) userRepository = BeanFactory.getBean(UserRepository.class);

        Long numberOfUserInDB = userRepository.count();

        System.out.printf("count user in DB: %s\n", numberOfUserInDB);

        System.out.format("count in form input: %s\n", valueInForm);
        if (valueInForm == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("count must be not null LOL")
                    .addConstraintViolation();
            return false;
        } else if (valueInForm <= numberOfUserInDB) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format(context.getDefaultConstraintMessageTemplate(), numberOfUserInDB))
                    .addConstraintViolation();
            return false;
        } else return true;
    }

}
