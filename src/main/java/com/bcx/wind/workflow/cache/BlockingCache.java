package com.bcx.wind.workflow.cache;

/**
 * 阻塞缓存
 * @param <K>
 * @param <V>
 *
 * @author zhanglei
 */
public class BlockingCache<K,V> extends BaseCache<K,V> {

    public BlockingCache(){
        super();
    }

    public BlockingCache(Cache<K, V> cache) {
        super(cache);
    }


    @Override
    public V get(K key) {
        return super.get(key);
    }

    @Override
    public V remove(K key) {
        synchronized (cache) {
            return super.remove(key);
        }
    }

    @Override
    public V put(K key, V value) {
        synchronized (cache) {
            return super.put(key, value);
        }
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public void clear() {
        super.clear();
    }
}
