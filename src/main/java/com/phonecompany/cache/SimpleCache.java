package com.phonecompany.cache;

import java.util.concurrent.*;

public class SimpleCache<K, V> {

    private final ConcurrentMap<K, Future<V>> cache = new ConcurrentHashMap<>();

    SimpleCache(long expirationTime) {
        //invokes cache clearing at each requested repetitive time period
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::clear,
                expirationTime, expirationTime, TimeUnit.SECONDS);
    }

    public void put(K key, V value) {
        createIfAbsent(key, () -> value);
    }

    private void createIfAbsent(final K key, final Callable<V> callable) {
        Future<V> future = cache.get(key);
        if (future == null) {
            final FutureTask<V> futureTask = new FutureTask<>(callable);
            future = cache.putIfAbsent(key, futureTask);
            if (future == null) {
                // Compute the value
                futureTask.run();
            }
        }
    }

    V getValue(final K key)
            throws InterruptedException, ExecutionException {
        Future<V> future = cache.getOrDefault(key, null);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clean-up the cache entries.
     */
    public void clear() {
        cache.clear();
    }

    public int getSize() {
        return cache.size();
    }

    boolean contains(K key) {
        return cache.containsKey(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }
}
