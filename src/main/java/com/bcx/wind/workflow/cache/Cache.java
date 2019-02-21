package com.bcx.wind.workflow.cache;

/**
 * 缓存接口
 * @param <K>
 * @param <V>
 *
 * @author zhanglei
 */
public interface Cache<K,V> {

    /**
     * 获取值
     * @param key 键
     * @return 值
     */
    V  get(K key);


    /**
     * 删除值
     * @param key  键
     * @return     删除的值
     */
    V  remove(K key);


    /**
     * 添加键值
     * @param key    键
     * @param value  值
     * @return       添加的值
     */
    V  put(K key, V value);


    /**
     * 缓存长度
     * @return  数量
     */
    int size();


    /**
     * 缓存是否为空
     * @return  boolean
     */
    boolean  isEmpty();


    /**
     * 清除缓存
     */
    void clear();

}
