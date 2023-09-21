package com.example.springbootdemo.handle.handle_exception;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.PessimisticLockException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HandleException {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object notRead2(HttpMessageNotReadableException ex, WebRequest request) {
        System.out.println(ex.getMessage());
        System.out.println(request);
        return "lol 1";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object notRead3(MethodArgumentNotValidException ex, WebRequest request) {
        System.out.println("1: ==> " + ex.getMessage());
        System.out.println("2: ==> " + request);
        Map<String, Object> errors = new HashMap<>();
        ex.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), ex.getFieldErrors(fieldError.getField()));
        });
        return errors;
    }

    @ExceptionHandler(IllegalStateException.class)
    public Object notRead4(IllegalStateException ex, WebRequest request) {
        System.out.println("3: ==> " + ex.getMessage());
        System.out.println("4: ==> " + request);
        return "lol 3";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object notRead5(ConstraintViolationException ex, WebRequest request) {
        System.out.println("5: ==> " + ex.getMessage());
        ex.getConstraintViolations()
                .forEach(v -> System.out.println(v.getConstraintDescriptor() + "=>" + v.getMessage()));
        return "constraint ex";
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public Object error(ObjectOptimisticLockingFailureException ex, WebRequest request) {
        System.out.println("6: ==> " + ex.getMessage());
        return "optimistic error in controller advice\n" + ex.getMessage();
    }

    @ExceptionHandler(PessimisticLockException.class)
    public Object lockError(PessimisticLockException ex, WebRequest request) {
        System.out.println("7: ==> " + ex.getMessage());
        return "pessimistic lock error in controller advice\n" + ex.getMessage();
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public Object rollback(UnexpectedRollbackException ex, WebRequest request) {
        System.out.println("8: ==> " + ex.getMessage());
        return "unexpected rollback exception in controller advice: \n" + ex.getMessage();
    }
}
