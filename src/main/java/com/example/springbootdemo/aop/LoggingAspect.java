package com.example.springbootdemo.aop;

import com.example.springbootdemo.annotation.MyMethodAnnotation;
import com.example.springbootdemo.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private List<Object> objects;

    @Pointcut("execution(* com.example.springbootdemo.controller.AopController.get(..))")
    public void loggingPointcut() {
    }

    @Pointcut("execution(* com.example.springbootdemo.controller.AopController.ar*(int, String, *))")
    public void aroundPointcut1() {
    }

    @Pointcut("execution(* com.example.springbootdemo.controller.AopController.ar*(int, String, ..))")
    public void aroundPointcut2() {
    }

    @Pointcut("@annotation(com.example.springbootdemo.annotation.MyMethodAnnotation)")
    public void annotationPointcut() {
    }

    @Pointcut("execution(* com.example.springbootdemo.controller.AopController.*(..)) " +
            "&& @annotation( org.springframework.web.bind.annotation.PostMapping))")
    public void postMappingPointCut() {
    }

    @Before(value = "postMappingPointCut()")
    public void beforePostMapping(JoinPoint joinPoint) {
        objects = Arrays.stream(joinPoint.getArgs()).toList();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();

        System.out.println(objects);
        System.out.println(Arrays.toString(parameterNames));

        String value = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("address")) {
                value = (String) objects.get(i);
                System.out.println("in param");
                break;
            } else {
                try {
                    Field field = objects.get(i).getClass().getDeclaredField("address");
                    field.setAccessible(true);
                    value = (String) field.get(objects.get(i));
                    System.out.println("in form");
                    break;
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
        }
        System.out.println(value);

        System.out.println("//////////////////////////");
    }

    @AfterReturning(value = "postMappingPointCut()")
    public void afterPostMapping(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        System.out.println(Arrays.toString(joinPoint.getArgs()));
    }

    @Before(value = "annotationPointcut()")
    public void annotation(JoinPoint joinPoint) throws NoSuchMethodException {
        Annotation annotation =
                joinPoint.getTarget().getClass()
                        .getMethod("get", int.class)
                        .getAnnotation(MyMethodAnnotation.class);
        log.warn(annotation + "");
        log.warn(joinPoint.getSignature().toString());
    }

    @Order(0)
    @AfterReturning(value = "annotationPointcut()", returning = "customer")
    public Object annotationAfterReturn(JoinPoint joinPoint, Customer customer) {
        System.out.println(customer);
        customer.setName("fixed in aop");
        customer.setId(999);
        log.warn(Arrays.toString(joinPoint.getArgs()));
        return customer;
    }

    @Around(value = "aroundPointcut1()")
    public Object around1(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();

        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        log.info("in around 1 advise, object = " + object);
        if (object instanceof ResponseEntity<?> response) {
            Customer customer = (Customer) response.getBody();
            assert customer != null;
            customer.setName(customer.getName() + " fixed 1");
            response = new ResponseEntity<>(customer, HttpStatus.CREATED);
            return response;
        }
        return object;
    }

    @Around(value = "aroundPointcut2()")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.proceed();
        log.info("in around 2 advise, object = " + object);
        if (object instanceof ResponseEntity<?> response) {
            Customer customer = (Customer) response.getBody();
            assert customer != null;
            customer.setName(customer.getName() + " fixed 2");
            response = new ResponseEntity<>(customer, HttpStatus.NOT_FOUND);
            return response;
        }
        return object;
    }

    @Before(value = "loggingPointcut()")
    public void beforeLogging(JoinPoint joinPoint) {
        log.info("--> " + joinPoint.getSignature().toLongString());
        log.info("--> " + joinPoint.getSignature().toString());
        log.info("--> " + joinPoint.getSignature().toShortString());
        log.info(joinPoint.getKind());
        log.info(Arrays.toString(joinPoint.getArgs()));
        log.info(joinPoint.getTarget().toString());
        log.info(joinPoint.getSourceLocation().toString());
    }

    @AfterReturning(value = "loggingPointcut()", returning = "customer")
    public void afterReturnLogging(JoinPoint joinPoint, Object customer) {
        log.info(joinPoint.getSignature().toLongString());
        log.info("after return: " + customer);
        System.out.println(customer.getClass().getSimpleName());
        if (customer instanceof ResponseEntity<?> response) {
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        }
    }

    @AfterThrowing(value = "loggingPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        log.info(joinPoint.getSignature().toLongString());
        log.error("error, throwing::" + e.getMessage());
        System.out.println(e.getClass().getSimpleName());
    }

}
