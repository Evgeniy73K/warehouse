package org.mediasoft.warehouse.annotations;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MeasureExecutionTimeAspect {

    @Around("@annotation(measureExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint, MeasureExecutionTime measureExecutionTime) throws Throwable {
        var startTime = System.currentTimeMillis();

        var returnValue = joinPoint.proceed();

        var endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("{} executed in {}ms", joinPoint.getSignature(), executionTime);

        return returnValue;
    }
}