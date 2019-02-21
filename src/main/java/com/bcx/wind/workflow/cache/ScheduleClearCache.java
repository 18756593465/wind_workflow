package com.bcx.wind.workflow.cache;

/**
 * 定时清理缓存对象实现
 * @param <K>
 * @param <V>
 *
 * @author zhanglei
 */
public class ScheduleClearCache<K,V> extends BaseCache<K,V> {

    //清除间隔时间  默认毫秒  30秒
    private long clearTime = 30000;


    //上次清理时间
    private  long  lastClearTime = System.currentTimeMillis();

    public ScheduleClearCache(){
        super();
    }

    public ScheduleClearCache(Cache<K, V> cache) {
        super(cache);
    }

    public ScheduleClearCache(Cache<K,V> cache, long clearTime){
        super(cache);
        this.clearTime = clearTime;
    }


    /**
     * 校验是否到清除时间
     *
     * @return   Boolean
     */
    private boolean checkOutTime(){
        return System.currentTimeMillis() - this.lastClearTime > this.clearTime;
    }

    private void scheduleClear(){
        if(checkOutTime()){
            this.clear();
        }
    }

    @Override
    public V get(K key) {
        scheduleClear();
        return super.get(key);
    }

    @Override
    public V remove(K key) {
        scheduleClear();
        return super.remove(key);
    }

    @Override
    public V put(K key, V value) {
        scheduleClear();
        return super.put(key,value);
    }

    @Override
    public int size() {
        scheduleClear();
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        scheduleClear();
        return super.isEmpty();
    }

    @Override
    public void clear() {
        this.lastClearTime = System.currentTimeMillis();
        super.clear();
    }
}
