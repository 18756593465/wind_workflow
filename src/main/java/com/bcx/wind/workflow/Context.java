package com.bcx.wind.workflow;

public interface Context {


    void  put(String key,Object value);


    void  put(String key,Class value);


    boolean exist(String key);


    <T>T   getValue(Class<T> clazz);


    Object getValue(String key);

    void clear();

    Object remove(String key);

}
