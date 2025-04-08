package org.cryptoklineproject.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("execution(public * org.cryptoklineproject.*.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //log
        long startTime = System.currentTimeMillis();
        log.info("[RoleAspect] Before -> {}, args={}",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs())
        );

        Object result = joinPoint.proceed();

        //log cost time and record
        long cost = System.currentTimeMillis() - startTime;
        log.info("[LogAspect] After -> methodSignature={}, cost={}ms",
                joinPoint.getSignature(),
                cost);

        return result;
    }
}
