package com.example.springbootdemo.aop;

import com.example.springbootdemo.dto.AopDto;
import com.example.springbootdemo.dto.AopParam;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // pointcut trên abstract, interface đều có tác dụng với class con mặc dù class con ở package khác
    // với point cut trên abstract, interface thì chỉ áp dụng cho method trong abstract, interface đó và ABSTRACT METHOD trên các lớp cha
    // với point cut trên class chỉ áp dụng cho method trong chính class đó
    @Pointcut(value = """
            execution(* com.example.springbootdemo.service.impl.AopService.ex*(..))
            && args(i)
            """, argNames = "i")
    public void executionPointCut(int i) {
    }

    // KHÔNG áp dụng cho interface, abstract class khi chỉ có abstract
    // point cut trên concrete class sẽ khớp với method trong class này và các lớp cha trong CÙNG PACKAGE
    @Pointcut(value = """
            within(com.example.springbootdemo.service.AopSe*)
            """)
    public void withinPointCut() {
    }

    // annotation có trên interface, abstract class, concrete class đều được, NHƯNG
    // annotate trên abstract, concrete chỉ có tác dụng với method được THỰC THI trên chính concrete, abstract đó
    // annotate trên @Repository chỉ có tác dụng cho method được KHAI BÁO trong repo đó
    @Pointcut(value = """
            @within(com.example.springbootdemo.annotation.MyAnnotationOnClass)
            """)
//            OR
//            @within(org.springframework.stereotype.Repository)
    public void annoWithinPointCut() {
    }

    // dùng được với cả class, abstract
    // có tác dụng với tất cả method trên class implement và super, abstract, các interface của class impl đó
    // TRỪ các body method trên interface, abstract, super và class nhánh khác
    @Pointcut(value = """
            this(org.springframework.data.jpa.repository.JpaRepository)
            """)
//            this(org.springframework.data.jpa.repository.JpaRepository)
    public void thisPointCut() {
    }

    // dùng được với cả class, abstract
    // có tác dụng với tất cả method trên class implement và super, abstract, các interface của class impl đó
    @Pointcut(value = """
            target(org.springframework.data.jpa.repository.JpaRepository)
            """)
    public void targetPointCut() {
    }

    // chỉ dùng mỗi @target() dễ lỗi
    // KHÔNG có tác dụng trên abstract
    // chỉ dùng trên concrete, áp dụng cho tất cả method trên concrete và super, abstract
    @Pointcut(value = """
            @target(com.example.springbootdemo.annotation.MyAnnotationOnClass)
            &&
            within(com.example.springbootdemo..*)
            """)
    public void annoTargetPointCut() {
    }

    // áp dụng cho tất cả các method của bean có param khớp point cut, kể cả các method default của abstract super
    @Pointcut(value = """
            args(*,param,*)
            &&
            within(com.example.springbootdemo..*))
            """)
    public void argsPointCut(AopDto param) {
    }

    // anno có thể đặt trên abstract
    @Pointcut(value = """
            @args(*,
                  com.example.springbootdemo.annotation.AopArgs,
                  com.example.springbootdemo.annotation.AopArgs,
                  ..)
            &&
            within(com.example.springbootdemo..*)
            """)
    public void annoArgsPointCut() {
    }

    // tác dụng trên method được annotated trên concrete, và method được THỰC THI trên abstract, super
    @Pointcut(value = "@annotation(com.example.springbootdemo.annotation.MyAnnotationOnMethod)")
    public void annotationPointCut() {
    }

    // ====================================================================================
    // ====================================================================================
    // ====================================================================================


    // @Before(value = "executionPointCut(i)", argNames = "joinPoint,i")
    public void beforeExecution(JoinPoint joinPoint, int i) {
        System.out.println("AOP execution ==> " + i + " ==> " + joinPoint);
    }

    //    @Before(value = "withinPointCut()", argNames = "joinPoint")
    public void beforeWith(JoinPoint joinPoint) {
        System.out.println("AOP within ==> " + joinPoint);
    }

    //    @Before(value = "annoWithinPointCut()", argNames = "joinPoint")
    public void beforeAnnoWith(JoinPoint joinPoint) {
        System.out.println("AOP @within ==> " + joinPoint);
    }

        @Before(value = "targetPointCut()", argNames = "joinPoint")
    public void beforeTarget(JoinPoint joinPoint) {
        System.out.println("AOP target ==> " + joinPoint);
    }

    //    @Before(value = "annoTargetPointCut()", argNames = "joinPoint")
    public void beforeAnnoTarget(JoinPoint joinPoint) {
        System.out.println("AOP @target ==> " + joinPoint);
    }

    //    @Before(value = "argsPointCut(aopParam)", argNames = "joinPoint,aopParam")
    public void beforeArgs(JoinPoint joinPoint, AopDto aopParam) {
        System.out.println("AOP arg ==> " + aopParam + " ==> " + joinPoint);
    }

    //    @Before(value = "annoArgsPointCut()", argNames = "joinPoint")
    public void beforeAnnoArgs(JoinPoint joinPoint) {
        System.out.println("AOP @arg ==> " + joinPoint);
    }

        @Before(value = "thisPointCut()")
    public void beforeThisArgs(JoinPoint joinPoint) {
        System.out.println("AOP this ==> " + joinPoint);
    }

//    @Before(value = "annotationPointCut()")
    public void beforeAnnotation(JoinPoint joinPoint) {
        System.out.println("AOP @annotation ==> " + joinPoint);
    }
}
