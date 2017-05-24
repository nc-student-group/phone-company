package com.phonecompany.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks method denoting the fact that the results of its computation
 * will be stored in cache.
 *
 * <p>Existing caching mechanism implementation does not make decisions
 * whether already containing results should be complemented with each
 * other.</p>
 *
 * <p>Note that caching is performed through
 * {@link com.phonecompany.cache.CachingAspect}</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
}
