package com.example.springbootdemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.ResourceBundle;

@RestController
@RequestMapping(value = "/i18n")
public class I18nController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<?> i18n(@RequestParam(value = "languageCode", defaultValue = "") String languageCode) {
        Object[] params = new Object[]{"param 0", "param 1", "param 2"};
//        String msg = messageSource.getMessage("hello.alo", params, "default message lol", Locale.forLanguageTag(languageCode));
        String msg = messageSource.getMessage(new UserRegistrationMessage("nam", 20), Locale.forLanguageTag(languageCode));
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping(value = "/resource-bundle")
    public ResponseEntity<?> i18nResourceBundle(@RequestParam(value = "languageCode", defaultValue = "") String languageCode,
                                                Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages-common", Locale.forLanguageTag(languageCode));
        String res = String.format(resourceBundle.getString("hello.alo2"), "param 00", "param 11", "param 22");

        ResourceBundle resourceBundle2 = ResourceBundle.getBundle("i18n/messages-common", locale);
        String string = resourceBundle2.getString("hello.alo2");

        Locale localeInContext = LocaleContextHolder.getLocale();
        ResourceBundle resourceBundle3 = ResourceBundle.getBundle("i18n/messages-common", localeInContext);
        String string3 = resourceBundle3.getString("hello.alo2");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/param")
    public ResponseEntity<?> i18nParam(Locale locale, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getHeader("Cookie"));
        String msg = messageSource.getMessage("hello.alo", null, locale);
        response.setHeader("ok", "oklol");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    static class UserRegistrationMessage implements MessageSourceResolvable {
        private final String username;
        private final int age;

        public UserRegistrationMessage(String username, int age) {
            this.username = username;
            this.age = age;
        }

        @Override
        public String[] getCodes() {
            return new String[]{"user.registration.message3", "user.registration.message", "user.registration.message2"};
        }

        @Override
        public Object[] getArguments() {
            return new Object[]{username, age};
        }

        @Override
        public String getDefaultMessage() {
            return "Welcome, {0}! Your age is {1}.";
        }
    }
}

