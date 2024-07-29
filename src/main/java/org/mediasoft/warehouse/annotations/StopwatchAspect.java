package org.mediasoft.warehouse.annotations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class StopwatchAspect {

    @Around("@annotation(stopwatch)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, Stopwatch stopwatch) throws Throwable {
        var startTime = System.currentTimeMillis();

        var returnValue = joinPoint.proceed();

        var endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("{} executed in {}ms", joinPoint.getSignature(), executionTime);

        return returnValue;
    }
}