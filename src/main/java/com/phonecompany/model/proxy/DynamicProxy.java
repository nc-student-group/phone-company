package com.phonecompany.model.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Represents a type to define a substitution object for the
 * type <T>. Real object will be loaded only after the first
 * call to either of its methods. Until that point it will
 * be just a proxy wrapper.
 *
 * @param <T> proxy type
 */
public class DynamicProxy<T> implements MethodInterceptor {

    private T source = null; // real object should be lazily loaded
    private Long sourceId;
    private Function<Long, T> mapper;

    private DynamicProxy(Long sourceId,
                         Function<Long, T> mapper) {
        this.sourceId = sourceId;
        this.mapper = mapper;
    }

    /**
     * Loads real object only when it was requested
     *
     * @return real object
     */
    private T getSource() {
        if (source == null) {
            source = mapper.apply(sourceId);
        }
        return source;
    }

    /**
     * Creates a proxy object of type inferred by the generic type of the
     * {@code SourceMapper}.
     *
     * @param sourceId     id that will be used by the respective mapper to
     *                     load an object
     * @param sourceMapper object which is used to load an entity by its id
     * @param <T>          type of the entity to load
     *
     * @return             CGlib proxy object
     * @see                SourceMappers
     */
    public static <T> T newInstance(Long sourceId, SourceMapper<T> sourceMapper) {

        Class<?> type = sourceMapper.getType();
        Function<Long, T> mapper = sourceMapper.getMapper();

        /**
         * It is safe to make this kind of cast because due to
         * {@code Class<?> type = sourceMapper.getType();}
         * CGlib creates a proxy based on the generic type of the
         * {@code SourceMapper} object which defines a return type
         * of the method
         */
        //noinspection unchecked
        return (T) Enhancer.create(type,
                new DynamicProxy<>(sourceId, mapper));
    }

    /**
     * Intercepts calls to the methods of the proxy to provide required functionality
     *
     * @param obj          the enhanced object
     * @param method       intercepted Method
     * @param args         argument array; primitive types are wrapped
     * @param methodProxy  used to invoke super (non-intercepted method); may be called
     *                     as many times as needed
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        T source = this.getSource(); // loads real object
        return method.invoke(source, args);
    }
}

