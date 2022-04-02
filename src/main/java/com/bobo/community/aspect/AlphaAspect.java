package com.bobo.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AlphaAspect {
//    @Pointcut("execution(* com.bobo.community.service.*.*(..))")
//    public void pointcut(){
//    }
//
//    @Before("pointcut()")
//    public void before(){
//        System.out.println("before(1);");
//    }
//
//    @After("pointcut()")
//    public void after(){
//        System.out.println("before(2)");
//    }
//    @AfterReturning("pointcut()")
//    public void afterReturn(){
//        System.out.println("before(3)");
//    }
//    @AfterThrowing("pointcut()")
//    public void afterThrow(){
//        System.out.println("before(4)");
//    }
//
//    @Around("pointcut()")
//    public Object afterThrow(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("before(5)");
//        Object obj = joinPoint.proceed();
//        System.out.println("before(6)");
//        return obj;
//    }
}
