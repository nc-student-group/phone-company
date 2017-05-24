package com.phonecompany.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks method denoting the fact that its invocation will trigger cache
 * clearing.
 *
 * <p>Note, that this annotation is supposed to be used on methods that
 * modify state of any object from the set contained within the cache.
 * Discarding the old objects from cache helps to keep data updated</p>
 *
 * @see com.phonecompany.cache.CachingAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheClear {
}
