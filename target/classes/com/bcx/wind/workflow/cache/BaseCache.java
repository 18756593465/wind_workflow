package com.bcx.wind.workflow.cache;

/**
 * 缓存抽象基类
 *
 * @param <K>
 * @param <V>
 *
 * @author zhanglei
 */
public abstract class BaseCache<K,V> implements Cache<K,V> {

    /**
     * 实际缓存对象
     */
    protected final Cache<K,V> cache;


    public BaseCache(){
        cache = new DefaultCache<>();
    }

    public BaseCache(Cache<K,V> cache){
        this.cache = cache;
    }

    @Override
    public V get(K key) {
        return this.cache.get(key);
    }

    @Override
    public V remove(K key) {
        return this.cache.remove(key);
    }

    @Override
    public V put(K key, V value) {
        return this.cache.put(key,value);
    }

    @Override
    public int size() {
        return this.cache.size();
    }

    @Override
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    @Override
    public void clear() {
        this.cache.clear();
    }
}
