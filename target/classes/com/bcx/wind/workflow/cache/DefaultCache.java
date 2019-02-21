package com.bcx.wind.workflow.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认缓存对象实现
 *
 * @param <K>
 * @param <V>
 *
 * @author zhanglei
 */
public class DefaultCache<K,V>  implements Cache<K,V> {

    private final Map<K,V> cacheMap = new ConcurrentHashMap<>();


    @Override
    public V get(K key) {
        return this.cacheMap.get(key);
    }

    @Override
    public V remove(K key) {
        return this.cacheMap.remove(key);
    }

    @Override
    public V put(K key, V value) {
        return this.cacheMap.put(key,value);
    }

    @Override
    public int size() {
        return this.cacheMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.cacheMap.isEmpty();
    }

    @Override
    public void clear() {
        this.cacheMap.clear();
    }
}
