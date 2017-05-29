package com.phonecompany.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.List;

/**
 * Class that implements caching capabilities
 */
@Aspect
@Component
public class CachingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(CachingAspect.class);

    public static final int CACHE_CLEANUP_COUNTDOWN = 200;
    //All values from cache will be cleared before JVM throws OutOfMemoryError
    private final SimpleCache<Pair<String, List<Object>>, SoftReference<Object>> cache =
            new SimpleCache<>(CACHE_CLEANUP_COUNTDOWN);

    /**
     * Places the result of method invocation into the cache.
     * <p>
     * <p>Method will not perform its calculation if it is being invoked with
     * the same set of parameters, already existing value from cache will be
     * returned instead.</p>
     *
     * @param joinPoint point in a program execution where caching aspect is
     *                  being inserted into
     * @return result of an actual method invocation or an already calculated value
     * @throws Throwable if an exception during method execution occurs
     */
    @Around("@annotation(com.phonecompany.annotations.Cacheable)+")
    public Object cacheResult(ProceedingJoinPoint joinPoint)
            throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        LOG.debug("Caching triggered for method: {}", methodName);
        List<Object> methodArguments = Arrays.asList(joinPoint.getArgs());
        Pair<String, List<Object>> resultIdentifier = Pair.with(methodName, methodArguments);
        if (cache.contains(resultIdentifier)) {
            Object existingValue = cache.getValue(resultIdentifier).get();
            LOG.debug("Returning existing value: {}", existingValue);
            return existingValue;
        } else {
            Object calculatedResult = joinPoint.proceed();
            SoftReference<Object> softResult = new SoftReference<>(calculatedResult);
            cache.put(resultIdentifier, softResult);
            LOG.debug("A new value has been put into the cache: {}", calculatedResult);
            return calculatedResult;
        }
    }

    @Around("@annotation(com.phonecompany.annotations.CacheClear)")
    public Object clearCache(ProceedingJoinPoint joinPoint)
            throws Throwable {
        cache.clear();
        return joinPoint.proceed();
    }
}