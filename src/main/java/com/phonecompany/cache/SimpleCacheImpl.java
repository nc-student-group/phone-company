package com.phonecompany.cache;

import java.util.concurrent.*;

public class SimpleCacheImpl<K,V> {

    private final ConcurrentMap<K, Future<V>> cache = new ConcurrentHashMap<>();

    public SimpleCacheImpl(long expirationTime) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::clear,
                expirationTime, expirationTime, TimeUnit.SECONDS);
    }

    private Future<V> createIfAbsent(final K key, final Callable<V> callable) {
        Future<V> future = cache.get(key);
        if (future == null) {
            final FutureTask<V> futureTask = new FutureTask<>(callable);
            future = cache.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;

                // Compute the value
                futureTask.run();
            }
        }
        return future;
    }

    public V getValue(final K key, final Callable<V> callable)
            throws InterruptedException, ExecutionException {
        try {
            return createIfAbsent(key, callable).get();
        } catch (InterruptedException | ExecutionException | RuntimeException e) {
            cache.remove(key);
            throw e;
        }
    }

    /**
     * Clean-up the cache entries.
     */
    public void clear() {
        // Clear the cache
        cache.clear();
    }
}
