package com.phonecompany.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class CachingAspect {

    public static final int CACHE_CLEANUP_COUNTDOWN = 200;
    private SimpleCacheImpl<Pair<String, List<Object>>, Object> cache = new SimpleCacheImpl<>(CACHE_CLEANUP_COUNTDOWN);

    @Around("@annotation(com.phonecompany.annotations.Cacheable)+")
    public Object cacheResult(ProceedingJoinPoint joinPoint)
            throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        List<Object> methodArguments = Arrays.asList(joinPoint.getArgs());
        Pair<String, List<Object>> resultIdentifier = Pair.with(methodName, methodArguments);
        if(cache.contains(resultIdentifier)) {
            return cache.getValue(resultIdentifier);
        } else {
            Object methodResult = joinPoint.proceed();
            cache.put(resultIdentifier, methodResult);
            return methodResult;
        }
    }

    @Around("@annotation(com.phonecompany.annotations.CacheClear)")
    public Object clearCache(ProceedingJoinPoint joinPoint)
            throws Throwable {
        cache.clear();
        return joinPoint.proceed();
    }
}